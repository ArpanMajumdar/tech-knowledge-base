// Read CSV
const rowMapper = function (d) {
  return {
    Food: d.Food,
    Deliciousness: parseFloat(d.Deliciousness),
  };
};

d3.csv("data/food.csv", rowMapper).then((data, error) => {
  if (error) {
    console.log("Error occurred while loading CSV", error);
  } else {
    console.log(data);
  }
});

// Enter method
const dataset = [5, 10, 15, 20, 25];
d3.select(".canvas")
  .selectAll("p")
  .data(dataset)
  .enter()
  .append("p")
  .text((d) => `The value of the current data point is ${d}`)
  .style("font-size", (d) => `${d * 2}px`)
  .style("color", (d) => {
    if (d <= 15) return "red";
    else return "black";
  });

