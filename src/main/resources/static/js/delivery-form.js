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
  pickupCell.innerHTML = `<input list="pickupOptions" id="legs${rowNumber}.pickup-text" class="form-control" placeholder="Select Pickup Location" required="required"/><input type="hidden" id="legs${rowNumber}.pickup" name="legs[${rowNumber}].pickup"/>`;
  let dropoffCell = row.insertCell();
  dropoffCell.innerHTML = `<div class="input-group"><input type="text" id="legs${rowNumber}.dropoff-text" class="form-control" placeholder="Add Dropoff Location"/><div class="input-group-append"><button id="legs${rowNumber}.dropoff-button" class="btn btn-secondary" type="button" onclick="addLocation(this);">+</button></div><input type="hidden" id="legs${rowNumber}.dropoff" name="legs[${rowNumber}].dropoff"/></div>`;
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

function addLocation(element) {
  let form = document.querySelector("#deliveryForm");
  let updateId = form.getAttribute("action").split('/')[2];
  form.setAttribute("action", `/delivery-form/location/new?legIndex=${element.id[4]}&updateId=${updateId}`);
  submitDelivery();
  form.submit();
}

function submitDelivery() {
  let listInputs = document.querySelectorAll('input[list]');
  let pickupOptions = document.querySelectorAll('#pickupOptions option');
  for (let input of listInputs) {
    let hiddenInput = document.getElementById(input.getAttribute('id').slice(0, -5));
    let inputText = input.value;
    hiddenInput.value = -1;
    for (let option of pickupOptions) {
      if (option.innerText === inputText) {
        hiddenInput.value = option.getAttribute('data-value');
      }
    }
  }
}

function updateFormTotal() {
  let total = 0;
  total += Number(document.getElementById("basePay").value);
  total += Number(document.getElementById("adjustments").value);
  for (let i = 0; i < tbody.rows.length; i++) {
    total += Number(document.getElementById(`legs${i}.tip`).value);
  }
  let spanTotal = document.getElementById("total");
  spanTotal.innerText = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(total);
}
