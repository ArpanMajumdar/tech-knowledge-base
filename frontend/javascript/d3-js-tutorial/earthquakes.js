const canvas = d3.select(".canvas");
const width = "100%";
const height = "600";

const svg = canvas.append("svg").attr("width", width).attr("height", height);
const earthquakeApiUrl =
  "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson";

const div = d3
  .select("body")
  .append("div")
  .attr("class", "tooltip")
  .style("opacity", 0);

// Parse JSON
d3.json(earthquakeApiUrl).then((data) => {
  const circle = svg.selectAll("circle").data(data["features"]);

  circle
    .enter()
    .append("circle")
    .attr("cx", (d) => d["geometry"]["coordinates"][0] + 300)
    .attr("cy", (d) => d["geometry"]["coordinates"][1] + 200)
    .attr("r", (d) => d["properties"]["mag"] * 2)
    .attr("fill", (d, i, n) => {
      // console.log(n[i]);
      const alertColor = d["properties"]["alert"];
      if (alertColor === null) {
        return "red";
      } else {
        return alertColor;
      }
    })
    .attr("stroke", "black")
    .on("mouseover", function (event, d) {
      d3.select(this).transition().duration(100).style("opacity", 0.3);

      div.transition().duration(200).style("opacity", 0.8);
      div
        .html(
          `<p>Mag: ${d["properties"]["mag"]}<\p>
        <p>Date: ${longTimestampToDate(d["properties"]["time"])}<\p>
        <p>Place: ${extractLocation(d["properties"]["place"])}`
        )
        .style("left", `${event.pageX}px`)
        .style("top", `${event.pageY - 40}px`);
    })
    .on("mouseout", function (event, d) {
      d3.select(this).transition().duration(100).style("opacity", 1);
      div.transition().duration(400).style("opacity", 0);
    });
});

function longTimestampToDate(timestamp) {
  return new Date(timestamp).toLocaleDateString("en-US");
}

function extractLocation(place) {
  return place.split(",")[1].trim();
}
