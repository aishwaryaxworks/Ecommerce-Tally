function showToast(message) {
    Swal.fire({
        toast: true,
        icon: 'success',
        title: message,
        position: 'bottom-end',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true
    });
}

function fetchMake() {
    let groupName = $("#groupName").val();
    let makeSelect = $("#make");
    makeSelect.empty().append('<option value="">Select Make</option>');
    if (validateGroupName() && groupName) {
        $.getJSON(`${urlConstant.url}/api/order/make/${groupName}`, function(makeList) {
            makeList.forEach(make => makeSelect.append(`<option value="${make}">${make}</option>`));
            makeSelect.selectpicker("destroy").selectpicker();
        }).fail(error => console.error("Error fetching makes:", error));
    }
}

function fetchModels() {
    let groupName = $("#groupName").val();
    let make = $("#make").val();
    let modelSelect = $("#model");
    let productCodeSelect = $("#productCode");

    if (validateMake() && make) {
        $.getJSON(`${urlConstant.url}/api/order/models/${groupName}/${make}`, function(models) {
            modelSelect.empty().append('<option value="" selected>Select Model</option>');
            models.forEach(model => modelSelect.append(`<option value="${model}">${model}</option>`));
            modelSelect.selectpicker("destroy").selectpicker();
                        productCodeSelect.empty().append('<option value="" selected>Select Product Code</option>');
            productCodeSelect.selectpicker("destroy").selectpicker();
        }).fail(error => console.error("Error fetching models:", error));
    }
    clearItemFields();
}

function fetchProductCodes() {
    let groupName = $("#groupName").val();
    let make = $("#make").val();
    let model = $("#model").val();
    let productCodeSelect = $("#productCode");
    if (validateGroupName() && validateMake() && validateModel() && make && model) {
        $.getJSON(`${urlConstant.url}/api/order/productCodes/${groupName}/${make}/${model}`, function(productCodes) {
            productCodeSelect.empty().append('<option value="" selected>Select Product Code</option>');
            productCodes.forEach(code => productCodeSelect.append(`<option value="${code}">${code}</option>`));
            productCodeSelect.selectpicker("destroy").selectpicker();
        }).fail(error => console.error("Error fetching product codes:", error));
    }
    clearItemFields();
}

function fetchPriceAndStock() {
  let groupName = $("#groupName").val();
  let make = $("#make").val();
  let model = $("#model").val();
  let productCode = $("#productCode").val();
  if (validateGroupName() && validateMake() && validateModel() && validateProductCode() && make && model && productCode) {
    $.get(`${urlConstant.url}/api/order/inventory/${groupName}/${make}/${model}/${productCode}`, function(data) {
      $("#openingValue").val(data.openingValue);
      $("#openingBalance").val(data.openingBalance);
      $("#itemName").val(data.itemName);
    });
  }
}


function validateOrderType(){
 let voucherType = document.getElementById("voucherType").value;
  let isValid = true;
  if (!voucherType) {
    document.getElementById("orderTypeError").style.display = "block";
    isValid = false;
  } else {
    document.getElementById("orderTypeError").style.display = "none";
    isValid = true;
  }
  return isValid;
}

function fetchMake() {
    let groupName = $("#groupName").val();
    let makeSelect = $("#make");

    if (groupName) {
        $.getJSON(`${urlConstant.url}/api/order/make/${groupName}`, function(makeList) {
            makeSelect.empty().append('<option value="" selected>Select Make</option>');
            makeList.forEach(make => makeSelect.append(`<option value="${make}">${make}</option>`));
            makeSelect.selectpicker("destroy").selectpicker();
            clearModelAndProductCode();
        }).fail(error => console.error("Error fetching makes:", error));
    }
    clearItemFields();
}

function clearItemFields() {
    $("#itemName").val('');
    $("#openingValue").val('');
    $("#openingBalance").val('');
}

function clearModelAndProductCode() {
    let modelSelect = $("#model");
    let productCodeSelect = $("#productCode");
    let makeSelect = $("#make");
      makeSelect.val('').selectpicker("destroy").selectpicker();
      modelSelect.val('').selectpicker("destroy").selectpicker();
      productCodeSelect.val('').selectpicker("destroy").selectpicker();
}

$("#groupName").on("change", function() {
    clearModelAndProductCode();
    clearItemFields();
});

function validateCustomerName() {
  let customer = document.getElementById("customer").value;
  let isValid = true;
  if (!customer) {
    document.getElementById("customerError").style.display = "block";
    isValid = false;
  } else {
    document.getElementById("customerError").style.display = "none";
    isValid = true;
  }
  return isValid;
}

function validateGroupName(){
 let groupName = document.getElementById("groupName").value;
  let isValid = true;
  if (!groupName) {
    document.getElementById("groupNameError").style.display = "block";
    isValid = false;
  } else {
    document.getElementById("groupNameError").style.display = "none";
    isValid = true;
  }
  return isValid;
}

function validateMake(){
 let make = document.getElementById("make").value;
  let isValid = true;
  if (!make) {
    document.getElementById("makeError").style.display = "block";
    isValid = false;
  } else {
    document.getElementById("makeError").style.display = "none";
    isValid = true;
  }
  return isValid;
}

function validateModel(){
 let  model= document.getElementById("model").value;
  let isValid = true;
  if (!model) {
    document.getElementById("modelError").style.display = "block";
    isValid = false;
  } else {
    document.getElementById("modelError").style.display = "none";
    isValid = true;
  }
  return isValid;
}

function validateProductCode(){
 let  productCode= document.getElementById("productCode").value;
  let isValid = true;
  if (!productCode) {
    document.getElementById("productCodeError").style.display = "block";
    isValid = false;
  } else {
    document.getElementById("productCodeError").style.display = "none";
    isValid = true;
  }
  return isValid;
}


function validateNarration() {
    let isValid = true;
    let sellingPrice = parseFloat(document.getElementById("sellingPrice").value);
    let openingValue = parseFloat(document.getElementById("openingValue").value);
    let narration = document.getElementById("narration").value.trim();
    if (openingValue > sellingPrice) {
        if (narration === "") {
            document.getElementById("narrationError").textContent = "Narration is required when Opening Value is less than Selling Price.";
            isValid = false;
        } else {
            document.getElementById("narrationError").textContent = "";
        }
    } else {
        document.getElementById("narrationError").textContent = "";
    }

    return isValid;
}

function setDefaultOrderDueDate() {
    let orderDueDateInput = document.getElementById("orderDueDate");
    if (!orderDueDateInput.value) {
        let tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        let formattedDate = tomorrow.toISOString().split('T')[0];
        orderDueDateInput.value = formattedDate;
    }
}

function validateForm() {
  const isValid = [validateOrderType(),validateCustomerName(),validateGroupName()
  ,validateMake(),validateModel(),validateProductCode(), validateSellingPrice(),validateNarration()].every(
    Boolean
  );
  if (isValid) {
    let modal = new bootstrap.Modal(document.getElementById("buyModal"));
    modal.show();
  }
}

function validateSellingPrice() {
  let isValid = true;
  let sellingPrice = document.getElementById("sellingPrice").value;
  let ratePerItem = document.getElementById("openingValue").value;

  if (sellingPrice === "") {
    document.getElementById("sellingPriceError1").style.display = "block";
    isValid = false;
  }else {
    document.getElementById("sellingPriceError1").style.display = "none";
    isValid = true;
  }
  return isValid;
}

function validateQuantity() {
  let quantity = document.getElementById("quantity").value;
  let stockInHand = parseInt(document.getElementById("openingBalance").value);
  let isValid = true;
  if(!quantity){
    document.getElementById("quantityError1").style.display = "block";
    isValid = false;
  }else if (quantity.includes(".")) {
    document.getElementById("quantityError2").style.display = "block";
    isValid = false;
  } else if (parseInt(quantity) > stockInHand) {
    document.getElementById("quantityError3").style.display = "block";
    isValid = false;
  } else {
    document.getElementById("quantityError3").style.display = "none";
    document.getElementById("quantityError1").style.display = "none";
    document.getElementById("quantityError2").style.display = "none";
    isValid = true;
  }
  return isValid;
}

function calculateTotalPrice() {
  let quantity = document.getElementById("quantity").value;
  let sellingPrice = document.getElementById("sellingPrice").value;
  let totalPriceField = document.getElementById("totalPrice");
  let hiddenQuantity = document.getElementById("hiddenQuantity");
  let hiddenTotalPrice = document.getElementById("hiddenTotalPrice");
  if (validateQuantity() && quantity && sellingPrice) {
    let totalPrice = quantity * sellingPrice;
    totalPriceField.value = totalPrice;
    hiddenQuantity.value = quantity;
    hiddenTotalPrice.value = totalPrice;
  }
}

function setInvoice() {
    let isValid = true;
    let isInvoice = document.querySelector('input[name="isInvoice"]:checked')?.value;
    if (!isInvoice) {
        document.getElementById("isInvoiceError").style.display = "block";
        isValid = false;
    } else {
        document.getElementById("isInvoiceError").style.display = "none";
        document.getElementById('hiddenIsInvoice').value = isInvoice;
        isValid = true;
    }
    return isValid;
}

function submitOrder() {
    const isValid = [validateOrderType(),validateCustomerName(),validateGroupName(),
        validateMake(),validateModel(),validateProductCode(),validateSellingPrice(),
        validateNarration(),setInvoice(),validateQuantity()].every(Boolean);
    if (isValid) {
        setDefaultOrderDueDate();
        document.getElementById("orderForm").submit();
    }
}

function updateOrderSubmit() {
    const isValid = [validateOrderType(),validateCustomerName(),validateGroupName(),
        validateMake(),validateModel(),validateProductCode(),validateSellingPrice(),
        validateNarration(),setInvoice(),validateQuantity()].every(Boolean);
    return isValid;
}


//function submitOrder() {
//
// const isValid = [validateOrderType(),validateCustomerName(),validateGroupName()
//  ,validateMake(),validateModel(),validateProductCode(), validateSellingPrice(),validateNarration(),setInvoice(),validateQuantity()].every(
//    Boolean
//  );
//  if (validateQuantity()) {
//    setDefaultOrderDueDate();
//    setInvoice();
//    document.getElementById("orderForm").submit();
//  }
//}



function fetchByCustomerName() {
    let customerName = document.getElementById("customer").value;
    if (validateCustomerName() && customerName) {
        fetch(`${urlConstant.url}/api/customer/byCustomerName?customerName=${encodeURIComponent(customerName)}`)
            .then((response) => response.json())
            .then((returnValue) => {
            document.getElementById("customerId").value = returnValue.customerId;
                let payableAmount = returnValue && returnValue.customerPayableAmount ? returnValue.customerPayableAmount : 0;
                document.getElementById("customerPayableAmount").value = payableAmount;
            })
            .catch((error) => console.error("Error fetching customer data:", error));
    }
}

function validateSellingPrice() {
    let sellingPrice = parseFloat(document.getElementById("sellingPrice").value);
    let openingValue = parseFloat(document.getElementById("openingValue").value);
    let errorField = document.getElementById("sellingPriceError");
    errorField.style.display = "none";
    errorField.style.color = "red";

    if (isNaN(sellingPrice) || sellingPrice <= 0) {
        errorField.style.display = "block";
        errorField.innerText = "Please enter a valid selling price.";
        return false;
    }
    let minAllowedPrice = openingValue * 0.98;
    if (sellingPrice < minAllowedPrice) {
        errorField.style.display = "block";
        errorField.innerText = "Selling price cannot be less than 2% of the rate per item.";
        return false;
    }
    if (sellingPrice < openingValue) {
        errorField.style.display = "block";
        errorField.style.color = "orange";
        errorField.innerText = `Warning: Selling price (${sellingPrice.toFixed(2)}) is lower than the opening value (${openingValue.toFixed(2)})!`;
    } else if (sellingPrice > openingValue) {
        errorField.style.display = "block";
        errorField.style.color = "blue";
        errorField.innerText = `Note: Selling price (${sellingPrice.toFixed(2)}) is higher than the opening value (${openingValue.toFixed(2)}).`;
    }
    return true;
}





