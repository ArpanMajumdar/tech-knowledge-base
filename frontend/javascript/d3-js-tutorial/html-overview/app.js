// This returns a D3 wrapped object
const canvas = d3.select(".canvas");
console.log(canvas);

// Add an SVG element
const svg = canvas.append("svg").attr("width", "600").attr("height", "600");

svg
  .append("rect")
  .attr("x", "200")
  .attr("y", "100")
  .attr("width", "100")
  .attr("height", "60")
  .attr("fill", "blue")
  .attr("rx", "15")
  .attr("ry", "15");

svg
  .append("circle")
  .attr("cx", "400")
  .attr("cy", "100")
  .attr("r", "50")
  .attr("fill", "green");

svg
  .append("line")
  .attr("x1", 100)
  .attr("x2", 200)
  .attr("y1", 100)
  .attr("y2", 200)
  .attr("stroke", "gray")
  .attr("stroke-width", 5);

svg
  .append("text")
  .text("Hello there..")
  .attr("text-anchor", "start")
  .attr("x", 100)
  .attr("y", 150)
  .attr("stroke", "green")
  .attr("font-size", 50);

svg
  .append("text")
  .text("How are you?")
  .attr("text-anchor", "middle")
  .attr("x", 200)
  .attr("y", 200)
  .attr("fill", "red")
  .attr("font-size", 50);
