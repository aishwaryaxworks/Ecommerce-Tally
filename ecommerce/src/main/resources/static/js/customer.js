function validateCustomerName() {
  let isValid = true;
  const customerName = document.getElementById("customerName").value;
  if (!customerName || customerName==="") {
    document.getElementById("customerNameError").textContent =
      "Customer Name is required.";
    isValid = false;
  } else if (customerName.length < 3) {
    document.getElementById("customerNameError").textContent =
      "Customer Name should be at least 3 characters.";
    isValid = false;
  } else {
    document.getElementById("customerNameError").textContent = "";
    isValid = true;
  }
  return isValid;
}
function validateCustomerType() {
    let isValid = true;
    const customerType = document.getElementById("customerType").value;

    if (!customerType || customerType === "") {
        document.getElementById("customerTypeError").textContent =
          "Please select a customer type.";
        isValid = false;
    } else {
        document.getElementById("customerTypeError").textContent = "";
        isValid=true;
    }
    return isValid;
}

function validateCustomerEmail() {
  let isValid = true;
  const customerEmail = document.getElementById("customerEmailId").value;
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!customerEmail || customerEmail==="") {
    document.getElementById("customerEmailError").textContent =
      "Email is required.";
    isValid = false;
  } else if (!emailPattern.test(customerEmail)) {
    document.getElementById("customerEmailError").textContent =
      "Invalid email address.";
    isValid = false;
  } else {
    document.getElementById("customerEmailError").textContent = "";
    isValid = true;
  }
  return isValid;
}
let initialEmailValue = document.getElementById("customerEmailId").value;
function validateEmail(){
 let currentEmailValue = document.getElementById("customerEmailId").value;
        if (currentEmailValue !== initialEmailValue) {
            customerEmailIdCheck();
        }
}
function validateContactNumber() {
  let isValid = true;
  const contactNumber = document.getElementById("contactNumber").value;
  if (!contactNumber || contactNumber ==="") {
    document.getElementById("contactNumberError").textContent =
      "Contact Number is required.";
    isValid = false;
  } else if (!/^[6-9]/.test(contactNumber)) {
    document.getElementById("contactNumberError").textContent =
      "Contact Number must start with 6, 7, 8, or 9";
    isValid = false;
  } else if (contactNumber.length !== 10) {
    document.getElementById("contactNumberError").textContent =
      "Contact Number must be exactly 10 digits long.";
    isValid = false;
  } else {
    document.getElementById("contactNumberError").textContent = "";
    isValid = true;
  }
  return isValid;
}
   let initialContactNumberValue = document.getElementById("contactNumber").value;
    function checkContactNumberChange() {
        let currentContactNumberValue = document.getElementById("contactNumber").value;
        if(validateContactNumber()){
          if (currentContactNumberValue !== initialContactNumberValue) {
            contactNumberCheck();
            contactNumberError("");
          }
        }else{
        contactNumberErrordb("");
        }
    }

function validateCountry() {
  let isValid = true;
  const country = document.getElementById("country").value;
  if (!country) {
    document.getElementById("countryError").textContent =
      "Country is required.";
    isValid = false;
  } else {
    document.getElementById("countryError").textContent = "";
    isValid = true;
  }
  return isValid;
}

function validateState() {
  let isValid = true;
  const state = document.getElementById("state").value;
  if (!state) {
    document.getElementById("stateError").textContent = "State is required.";
    isValid = false;
  } else {
    document.getElementById("stateError").textContent = "";
    isValid = true;
  }
  return isValid;
}
function validateStates() {
  let isValid = true;
  const state = document.getElementById("states").value;
  if (!state) {
    document.getElementById("stateError").textContent = "State is required.";
    isValid = false;
  } else {
    document.getElementById("stateError").textContent = "";
    isValid = true;
  }
  return isValid;
}

function validateForm() {
  const isValid = [
    validateCustomerName(),
    validateCustomerType(),
    validateCustomerEmail(),
    validateContactNumber(),
    validateCountry(),
    validateState(),
    validateCity(),
    validatePinCode(),
    validateAddress1(),
    validateBillingAddress(),
    validateShippingAddress(),
    validatePaymentModeOnline(),
  ].every(Boolean);
  return isValid;
}

function validateCity() {
  let isValid = true;
  const city = document.getElementById("city").value;
  if (!city) {
    document.getElementById("cityError").textContent = "City is required.";
    isValid = false;
  } else {
    document.getElementById("cityError").textContent = "";
    isValid = true;
  }
  return isValid;
}

function validatePinCode() {
  let isValid = true;
  const pinCode = document.getElementById("pinCode").value;
  const pinCodePattern = /^[0-9]{6}$/;
  if (!pinCode) {
    document.getElementById("pinCodeError").textContent =
      "Pin Code is required.";
    isValid = false;
  } else if (!pinCodePattern.test(pinCode)) {
    document.getElementById("pinCodeError").textContent =
      "Pin Code must be 6 digits.";
    isValid = false;
  } else {
    document.getElementById("pinCodeError").textContent = "";
    isValid = true;
  }
  return isValid;
}

function validateAddress1() {
  let isValid = true;
  const address1 = document.getElementById("address1").value;
  if (!address1) {
    document.getElementById("address1Error").textContent =
      "At List One Address  is required.";
    isValid = false;
  } else {
    document.getElementById("address1Error").textContent = "";
    isValid = true;
  }
  return isValid;
}

function validateBillingAddress() {
  let isValid = true;
  const billingAddress = document.getElementById("billingAddress").value;
  if (!billingAddress) {
    document.getElementById("billingAddressError").textContent =
      "Billing Address is required.";
    isValid = false;
  } else {
    document.getElementById("billingAddressError").textContent = "";
    isValid = true;
  }
  return isValid;
}

function validateShippingAddress() {
  let isValid = true;
  const shippingAddress = document.getElementById("shippingAddress").value;
  if (!shippingAddress) {
    document.getElementById("shippingAddressError").textContent =
      "Shipping Address is required.";
    isValid = false;
  } else {
    document.getElementById("shippingAddressError").textContent = "";
    isValid = true;
  }
  return isValid;
}

function validatePaymentModeOnline() {
  let isValid = true;
  const paymentModeOnline = document.getElementById("onlinePayment").checked;
  const paymentModeCheque = document.getElementById("chequePayment").checked;
  const paymentModeCash = document.getElementById("cashPayment").checked;
  if (!paymentModeOnline && !paymentModeCheque && !paymentModeCash) {
    document.getElementById("paymentModeError").textContent =
      "At least one Payment Mode is required.";
    isValid = false;
  } else {
    document.getElementById("paymentModeError").textContent = "";
    isValid = true;
  }
  return isValid;
}

function clearForm() {
  document.getElementById("customerForm").reset();
  document
    .querySelectorAll(".error")
    .forEach((error) => (error.textContent = ""));
}

async function loadStates() {
  const selectedCountry = document.getElementById("country").value;
  if (selectedCountry) {
    try {
      const response = await fetch(
        "https://countriesnow.space/api/v0.1/countries/states",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ country: selectedCountry }),
        }
      );
      const data = await response.json();
      const stateDropdown = document.getElementById("state");
      stateDropdown.innerHTML = '<option value="">Select State</option>';

      data.data.states.forEach((state) => {
        const option = document.createElement("option");
        option.value = state.name;
        option.textContent = state.name;
        stateDropdown.appendChild(option);
      });
    } catch (error) {
      console.error("Error fetching states:", error);
    }
  }
}
document.addEventListener("DOMContentLoaded", loadStates);
async function loadCities() {
  const selectedCountry = document.getElementById("country").value;
  const selectedState = document.getElementById("state").value;
  if (selectedState) {
    try {
      const response = await fetch(
        "https://countriesnow.space/api/v0.1/countries/state/cities",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ country: selectedCountry, state: selectedState }),
        }
      );
      const data = await response.json();
      const cityDropdown = document.getElementById("city");
      cityDropdown.innerHTML = '<option value="">Select City</option>';

      data.data.forEach((city) => {
        const option = document.createElement("option");
        option.value = city;
        option.textContent = city;
        cityDropdown.appendChild(option);
      });
    } catch (error) {
      console.error("Error fetching cities:", error);
    }
  }
}

function customerEmailIdCheck() {
    let customerEmailId = document.getElementById("customerEmailId").value;
    if (customerEmailId) {
        fetch(`${urlConstant.url}/api/customer/email?customerEmail=${encodeURIComponent(customerEmailId)}`)
            .then((response) => response.text())
            .then((returnValue) => {
                document.getElementById("customerEmailErrordb").textContent = returnValue;
            });
    }
}

function contactNumberCheck() {
    let contactNumber = document.getElementById("contactNumber").value;
    if (contactNumber) {
        fetch(`${urlConstant.url}/api/customer/contactNumber?contactNumber=${encodeURIComponent(contactNumber)}`)
            .then((response) => response.text())
            .then((returnValue) => {
                document.getElementById("contactNumberErrordb").textContent = returnValue;
            });
    }
}

function gstNumberCheck() {
    let gstNumber = document.getElementById("gstNumber").value;
    if (gstNumber) {
        fetch(`${urlConstant.url}/api/customer/customerGstNumber?customerGstNumber=${encodeURIComponent(gstNumber)}`)
            .then((response) => response.text())
            .then((returnValue) => {
                document.getElementById("gstNumberError").textContent = returnValue;
            });
    }
}

function checkCustomerName() {
    let customerName = document.getElementById("customerName").value;
    if (validateCustomerName()&&customerName) {
        fetch(`${urlConstant.url}/api/customer/checkCustomerName?customerName=${encodeURIComponent(customerName)}`)
            .then((response) => response.text())
            .then((returnValue) => {
                document.getElementById("customerNameError").textContent = returnValue;
            });
    }
}


 let initialCustomerName = document.getElementById("customerName").value;
    function customerNameValidation() {
        let currentCustomerName = document.getElementById("customerName").value;
        if(validateCustomerName()){
          if (currentCustomerName !== initialCustomerName) {
              checkCustomerName();
          }
        }
    }


 function getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}

function showActionToast(action, status) {
    let message = '';
    let icon = 'success';  // Default icon

    if (status === 'success') {
        if (action === 'save') {
            message = 'Customer saved successfully';
        } else if (action === 'update') {
            message = 'Customer updated successfully';
        } else if (action === 'delete') {
            message = 'Customer deleted successfully';
        }
    } else if (status === 'failure') {
        message = `Failed to ${action} inventory item`;
        icon = 'error';
    }

    if (message) {
        Swal.fire({
            toast: true,
            icon: icon,
            title: message,
            position: 'bottom-end',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true
        });
    }
}
var action = getUrlParameter('action');
var status = getUrlParameter('status');
if (action && status) {
    showActionToast(action, status);
}

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

function copyBillingAddress() {
  var billingAddress = document.getElementById("billingAddress");
  var shippingAddress = document.getElementById("shippingAddress");
  shippingAddress.value = billingAddress.value;
}

function clearShippingAddress() {
  var shippingAddress = document.getElementById("shippingAddress");
  shippingAddress.value = "";
}

