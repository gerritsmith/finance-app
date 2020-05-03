let addRowButton = document.getElementById("addRowButton");
let tbody = document.getElementById("formTableBody");

function addRow() {
  let rowNumber = tbody.rows.length;
  let row = tbody.insertRow();
  let foodTotalCell = row.insertCell();
  foodTotalCell.innerHTML = `<input type="text" class="form-control" name="legs[${rowNumber}].foodTotal"/>`;
  let tipCell = row.insertCell();
  tipCell.innerHTML = `<input type="text" class="form-control" name="legs[${rowNumber}].tip"/>`;
  let pickupCell = row.insertCell();
  pickupCell.innerHTML = `<input type="text" class="form-control" name="legs[${rowNumber}].pickup"/>`;
  let dropoffCell = row.insertCell();
  dropoffCell.innerHTML = `<input type="text" class="form-control" name="legs[${rowNumber}].dropoff"/>`;
  let noteCell = row.insertCell();
  noteCell.innerHTML = `<input type="text" class="form-control" name="legs[${rowNumber}].note"/>`;
}
