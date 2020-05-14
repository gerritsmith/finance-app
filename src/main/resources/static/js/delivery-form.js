let tbody = document.getElementById("formTableBody");
let originalRowState = [];
let originalNumberOfRows;

function addRow() {
  let rowNumber = tbody.rows.length;
  let row = tbody.insertRow(-1);
  let foodTotalCell = row.insertCell();
  foodTotalCell.innerHTML = `<input id="legs${rowNumber}.foodTotal" class="form-control" type="text" name="legs[${rowNumber}].foodTotal"/>`;
  let tipCell = row.insertCell();
  tipCell.innerHTML = `<input id="legs${rowNumber}.tip" class="form-control" type="text" name="legs[${rowNumber}].tip"/>`;
  let pickupCell = row.insertCell();
  pickupCell.innerHTML = `<select id="legs${rowNumber}.pickup" name="legs[${rowNumber}].pickup" class="form-control">${tbody.rows[0].cells[2].firstElementChild.innerHTML}</select>`;
  let dropoffCell = row.insertCell();
  dropoffCell.innerHTML = `<select id="legs${rowNumber}.dropoff" name="legs[${rowNumber}].dropoff" class="form-control">${tbody.rows[0].cells[3].firstElementChild.innerHTML}</select>`;
  let noteCell = row.insertCell();
  noteCell.innerHTML = `<input id="legs${rowNumber}.note" class="form-control" type="text" name="legs[${rowNumber}].note"/>`;
}

function removeRow() {
  if (tbody.rows.length > 1) {
    tbody.deleteRow(-1);
  }
}

function makeEditable() {
  for (row of tbody.rows) {
    originalRowState.push(row.innerHTML);
  }
  originalNumberOfRows = tbody.rows.length;
}

function cancelEdit() {
  let currentNumberOfRows = tbody.rows.length;
  if (tbody.rows.length < originalNumberOfRows) {
    for (let i = currentNumberOfRows; i < originalNumberOfRows; i++) {
      let row = tbody.insertRow(-1);
      row.innerHTML = originalRowState[i];
      for (let cell of row.children) {
        cell.children[0].readOnly = false;
      }
    }
  } else {
    for (let i = 0; i < currentNumberOfRows - originalNumberOfRows; i++) {
      tbody.deleteRow(-1);
    }
    originalRowState = [];
  }
}

function changeDeliveryEditState() {
  let addRowButton = document.getElementById("addRowButton");
  let removeRowButton = document.getElementById("removeRowButton");
  if (addRowButton.disabled) {
    makeEditable();
  } else {
    cancelEdit();
  }
  addRowButton.parentElement.hidden = !addRowButton.parentElement.hidden;
  addRowButton.disabled = !addRowButton.disabled;
  removeRowButton.disabled = !removeRowButton.disabled;
  changeEditState();
}
