const canvas = d3.select(".canvas");
const svg = canvas.append("svg").attr("width", 600).attr("height", 600);
const rect = svg.selectAll("rect");

d3.json("data.json").then((data) => {
  console.log(data);
  rect
    .data(data)
    .enter()
    .append("rect")
    .attr("width", (d) => d["width"])
    .attr("fill", (d) => d["fill"])
    .attr("x", (_d, i) => i * 30)
    .attr("height", (d) => d["height"] * 2)
    .attr("y", (d) => 200 - d["height"] * 2);
});
