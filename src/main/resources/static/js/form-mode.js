let inputFields = document.querySelectorAll("input.form-control");
let editButton = document.getElementById("editButton");
let cancelButton = document.getElementById("cancelButton");
let saveButton = document.getElementById("saveButton");

function changeEditState() {
  for (let input of inputFields) {
    input.readOnly = !input.readOnly;
  }
  editButton.disabled = !editButton.disabled;
  editButton.hidden = !editButton.hidden;
  cancelButton.disabled = !cancelButton.disabled;
  cancelButton.hidden = !cancelButton.hidden;
  saveButton.disabled = !saveButton.disabled;
  saveButton.parentElement.hidden = !saveButton.parentElement.hidden;
}
