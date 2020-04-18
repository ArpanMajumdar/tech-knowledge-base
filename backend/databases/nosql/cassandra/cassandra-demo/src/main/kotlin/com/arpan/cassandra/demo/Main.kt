package com.arpan.cassandra.demo

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.Row
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.util.*


fun main() {
    val logger = LoggerFactory.getLogger("Cassandra demo")

    val createDbSql = """
        CREATE KEYSPACE IF NOT EXISTS essentials
        WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
    """.trimIndent()

    val createTableSql = """
        CREATE TABLE IF NOT EXISTS essentials.movies_by_actor (
          actor TEXT,
          release_year INT,
          movie_id UUID,
          title TEXT,
          genres SET<TEXT>,
          rating FLOAT,
          PRIMARY KEY ((actor), release_year, movie_id)
        ) WITH CLUSTERING ORDER BY (release_year DESC, movie_id ASC);
    """.trimIndent()

    val insertDataSql = listOf(
        "insert into essentials.movies_by_actor (actor, release_year, movie_id, title, genres, rating)" +
                "values ('Emma Stone', 2016, 07b53137-c0e5-4372-8406-0c875d31d8ec, 'La la land', {'musical', 'drama'}, 10)",
        "insert into essentials.movies_by_actor (actor, release_year, movie_id, title, genres, rating)" +
                "values ('Brad Pitt', 2011, 140247d4-d2bc-4480-84e5-659c9a7a07e7, 'Tree of life', {'drama'}, 10)",
        "insert into essentials.movies_by_actor (actor, release_year, movie_id, title, genres, rating)" +
                "values ('Emma Stone', 2011, 31d20d85-6a67-4939-bdfb-cc43f162e56e, 'Birdman', {'drama'}, 10)"
    )

    val queryDataSql =
        "select actor, release_year, movie_id, title, genres, rating from essentials.movies_by_actor where actor = 'Emma Stone'"

    try {
        val cqlSession = CqlSession.builder()
            .addContactPoint(InetSocketAddress("127.0.0.1", 9042))
            .withLocalDatacenter("datacenter1")
            .build()

        val releaseVersionResult = cqlSession.execute("select release_version from system.local")
        val releaseVersionRow = releaseVersionResult.one()

        if (releaseVersionRow != null) {
            logger.info("release_version = ${releaseVersionRow.getString("release_version")}")
        }

        cqlSession.execute(createDbSql)
        cqlSession.execute(createTableSql)
        insertDataSql.forEach { sql ->
            cqlSession.execute(sql)
        }

        val moviesByEmmaStoneRs = cqlSession.execute(queryDataSql)
        moviesByEmmaStoneRs.forEach { row: Row? ->
            val moviesByActor = if (row != null) MoviesByActor(
                actor = row.getString("actor"),
                releaseYear = row.getInt("release_year"),
                movieId = row.getUuid("movie_id"),
                title = row.getString("title"),
                genres = row.getSet("genres", String::class.java),
                rating = row.getFloat("rating")
            ) else MoviesByActor()

            logger.info(moviesByActor.toString())
        }
    } catch (exception: Exception) {
        logger.error("Error connecting cassandra ...", exception)
    }
}

data class MoviesByActor(
    val actor: String? = null,
    val releaseYear: Int = 0,
    val movieId: UUID? = null,
    val title: String? = null,
    val genres: Set<String>? = null,
    val rating: Float = 0.0f
)