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
  dropoffCell.innerHTML = `<input list="dropoffOptions" id="legs${rowNumber}.dropoff-text" class="form-control" placeholder="Select Dropoff Location"/><input type="hidden" id="legs${rowNumber}.dropoff" name="legs[${rowNumber}].dropoff"/>`;
  let cashCell = row.insertCell();
  cashCell.innerHTML = `<input id="legs${rowNumber}.cash" class="form-control" type="text" name="legs[${rowNumber}].cash"/>`;
  let noteCell = row.insertCell();
  noteCell.innerHTML = `<input id="legs${rowNumber}.note" class="form-control" type="text" name="legs[${rowNumber}].note"/>`;
}

function removeRow() {
  if (tbody.rows.length > 1) {
    tbody.deleteRow(-1);
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
  let selectInputs = document.querySelectorAll("select");
  for (let input of selectInputs) {
    input.disabled = !input.disabled;
  }
  changeEditState();
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
        if (cell.children[0].tagName == "INPUT") {
          cell.children[0].readOnly = false;
        } else if (cell.children[0].tagName == "SELECT") {
          cell.children[0].disabled = false;
        }
      }
    }
  } else {
    for (let i = 0; i < currentNumberOfRows - originalNumberOfRows; i++) {
      tbody.deleteRow(-1);
    }
  }
  originalRowState = [];
}

function submitDelivery() {
  let listInputs = document.querySelectorAll('input[list]');
  console.log(listInputs);
  let dropoffOptions = document.querySelectorAll('#dropoffOptions option');
  for (let input of listInputs) {
    // let listName = input.getAttribute('list');
    let hiddenInput = document.getElementById(input.getAttribute('id').slice(0, -5));
    let inputText = input.value;
    hiddenInput.value = -1;
    // TODO: fix bug when different dropoff locations have the same address
    for (let option of dropoffOptions) {
      if (option.innerText === inputText) {
        hiddenInput.value = option.getAttribute('data-value');
      }
    }
  }
  return true;
}

function updateFormTotal() {
  let total = 0;
  total += Number(document.getElementById("basePay").value);
  total += Number(document.getElementById("adjustments").value);
  for (let i = 0; i < tbody.rows.length; i++) {
    total += Number(document.getElementById(`legs${i}.tip`).value);
  }
  console.log(total);
  let spanTotal = document.getElementById("total");
  spanTotal.innerText = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(total);
}
