const svgWidth = 600;
const svgHeight = 600;

const canvas = d3.select(".canvas");
const svg = canvas
  .append("svg")
  .attr("width", svgWidth)
  .attr("height", svgHeight);

const margin = { left: 40, right: 40, top: 40, bottom: 40 };
const plotWidth = svgWidth - margin.left - margin.right;
const plotHeight = svgHeight - margin.top - margin.bottom;
const plot = svg
  .append("g")
  .attr("width", plotWidth)
  .attr("height", plotHeight)
  .attr("transform", `translate(${margin.left}, ${margin.top})`);
const rect = plot.selectAll("rect");

const xAxisGroup = plot
  .append("g")
  .attr("transform", `translate(0, ${plotHeight})`);
const yAxisGroup = plot.append("g");

d3.json("data.json").then((data) => {
  const scaleX = d3
    .scaleBand()
    .domain(data.map((item) => item["fill"]))
    .range([0, plotWidth])
    .paddingInner(0.1)
    .paddingOuter(0.1);

  const scaleY = d3
    .scaleLinear()
    .domain([0, d3.max(data, (d) => d["height"])])
    .range([plotHeight, 0]);

  const xAxis = d3.axisBottom(scaleX);
  const yAxis = d3.axisLeft(scaleY);
  xAxisGroup.call(xAxis);
  yAxisGroup.call(yAxis);

  const barChart = rect.data(data).enter().append("rect");

  barChart
    .transition()
    .attr("y", (d) => scaleY(d["height"]))
    .ease(d3.easeBounceOut)
    .delay((d, i) => i * 100)
    .attr("width", (d) => scaleX.bandwidth())
    .attr("height", (d) => plotHeight - scaleY(d["height"]))
    .attr("fill", (d) => d["fill"])
    .attr("x", (d, i) => scaleX(d["fill"]));

  barChart
    .on("mouseover", function (event, d) {
      d3.select(this).transition().duration(100).style("opacity", 0.5);
    })
    .on("mouseout", function (event, d) {
      d3.select(this).transition().duration(100).style("opacity", 1);
    });
});
