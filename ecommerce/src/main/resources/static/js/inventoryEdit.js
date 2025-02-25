const originalValues = {};
const makeInput = document.getElementById("make");
const modelInput = document.getElementById("model");
const productCodeInput = document.getElementById("productCode");
const itemNameInput = document.getElementById("itemName");
const groupDropdown = document.getElementById("groupName");
const selectedGroupName = groupDropdown.getAttribute("data-selected");
const inventoryForm = document.getElementById("inventoryForm");

function validateGroupName() {
    var groupName = document.getElementById("groupName").value;
    var groupNameError = document.getElementById("groupNameError");

    if (groupName === "") {
        groupNameError.innerText = "Product Group Name is required.";
        return false;
    } else {
        groupNameError.innerText = "";
        return true;
    }
}

function validateMake() {
    var make = document.getElementById("make").value.trim();
    var makeError = document.getElementById("makeError");

    if (make === "") {
        makeError.innerText = "Make cannot be blank.";
        return false;
    } else {
        makeError.innerText = "";
        return true;
    }
}

function validateModel() {
    var model = document.getElementById("model").value.trim();
    var modelError = document.getElementById("modelError");

    if (model === "") {
        modelError.innerText = "Model cannot be blank.";
        return false;
    } else {
        modelError.innerText = "";
        return true;
    }
}

function validateProductCode() {
    var productCode = document.getElementById("productCode").value.trim();
    var productCodeError = document.getElementById("productCodeError");

    if (productCode === "") {
        productCodeError.innerText = "Product Code cannot be blank.";
        return false;
    } else {
        productCodeError.innerText = "";
        return true;
    }
}

function validateIgstRate() {
    var igstRate = document.getElementById("igstRate").value.trim();
    var igstRateError = document.getElementById("igstRateError");
    var regex = /^(?:\d{1,2}(?:\.\d{1,2})?)$/;

    if (igstRate === "") {
        igstRateError.innerText = "GST Rate is required.";
        return false;
    } else if (!regex.test(igstRate)) {
        igstRateError.innerText = "Enter a valid GST Rate.";
        return false;
    } else {
        igstRateError.innerText = "";
        return true;
    }
}

function validateHsnCode() {
    var hsnCode = document.getElementById("hsnCode").value.trim();
    var hsnCodeError = document.getElementById("hsnCodeError");

    if (hsnCode === "") {
        hsnCodeError.innerText = "HSN Code is required.";
        return false;
    } else if (!/^[0-9]{4,8}$/.test(hsnCode)) {
        hsnCodeError.innerText = "HSN Code must be 4 to 8 numeric digits.";
        return false;
    } else {
        hsnCodeError.innerText = "";
        return true;
    }
}

function validateOpeningBalance() {
    var openingBalance = document.getElementById("openingBalance").value.trim();
    var openingBalanceError = document.getElementById("openingBalanceError");

    if (openingBalance === "") {
        openingBalanceError.innerText = "Opening Balance is required.";
        return false;
    } else if (isNaN(openingBalance)) {
        openingBalanceError.innerText = "Opening Balance must be a number.";
        return false;
    } else {
        openingBalanceError.innerText = "";
        return true;
    }
}

function validateOpeningValue() {
    var openingValue = document.getElementById("openingValue").value.trim();
    var openingValueError = document.getElementById("openingValueError");

    if (openingValue === "") {
        openingValueError.innerText = "Opening Value is required.";
        return false;
    } else if (isNaN(openingValue)) {
        openingValueError.innerText = "Opening Value must be a number.";
        return false;
    } else {
        openingValueError.innerText = "";
        return true;
    }
}

function validateBaseUnit() {
    var baseUnit = document.getElementById("baseUnit").value;
    var baseUnitError = document.getElementById("baseUnitError");

    if (baseUnit === "") {
        baseUnitError.innerText = "Please select a unit of measure.";
        return false;
    } else {
        baseUnitError.innerText = "";
        return true;
    }
}

// Master validation function for form submission
function validateInventoryForm() {
    var isValid = true;

    if (!validateGroupName()) isValid = false;
    if (!validateMake()) isValid = false;
    if (!validateModel()) isValid = false;
    if (!validateProductCode()) isValid = false;
    if (!validateIgstRate()) isValid = false;
    if (!validateHsnCode()) isValid = false;
    if (!validateOpeningBalance()) isValid = false;
    if (!validateOpeningValue()) isValid = false;
    if (!validateBaseUnit()) isValid = false;
    return isValid;
}

// Event listeners for real-time validation
document.getElementById("groupName").addEventListener("change", validateGroupName);
document.getElementById("baseUnit").addEventListener("change", validateBaseUnit);
document.getElementById("make").addEventListener("input", validateMake);
document.getElementById("model").addEventListener("input", validateModel);
document.getElementById("productCode").addEventListener("input", validateProductCode);
document.getElementById("igstRate").addEventListener("input", validateIgstRate);
document.getElementById("hsnCode").addEventListener("input", validateHsnCode);
document.getElementById("openingBalance").addEventListener("input", validateOpeningBalance);
document.getElementById("openingValue").addEventListener("input", validateOpeningValue);

function populateGroupNames() {
    fetch(`${urlConstant.url}/api/stock-group/names`)
        .then(response => response.json())
        .then(groupNames => {
            groupDropdown.innerHTML = '<option value="" disabled>Select a Product Group</option>';
            groupNames.forEach(group => {
                const option = document.createElement('option');
                option.value = group;
                option.textContent = group;
                if (group === groupDropdown.getAttribute("data-selected")) {
                    option.selected = true;
                }
                groupDropdown.appendChild(option);
            });
        });
}

function updateItemName() {
    let make = makeInput.value.trim();
    let model = modelInput.value.trim();
    let productCode = productCodeInput.value.trim();

    let currentItemName = itemNameInput.value.trim();
    let itemParts = currentItemName.split("-");

    let updatedMake = make || (itemParts[0] || "");
    let updatedModel = model || (itemParts[1] || "");
    let updatedProductCode = productCode || itemParts.slice(2).join("-") || "";

    let newItemName = `${updatedMake}-${updatedModel}-${updatedProductCode}`.trim();
    itemNameInput.value = newItemName;
    itemNameInput.setAttribute("value", newItemName);
}

makeInput.addEventListener("input", updateItemName);
modelInput.addEventListener("input", updateItemName);
productCodeInput.addEventListener("input", updateItemName);

populateGroupNames();
