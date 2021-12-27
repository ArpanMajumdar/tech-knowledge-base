const canvas = d3.select(".canvas");
const width = "100%";
const height = "600";

const svg = canvas.append("svg").attr("width", width).attr("height", height);
const earthquakeApiUrl =
  "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson";

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
      console.log(d["properties"]["mag"]);
      d3.select(this).transition().duration(100).style("opacity", 0.3);
    })
    .on("mouseout", function (event, d) {
      d3.select(this).transition().duration(100).style("opacity", 1);
    });
});
