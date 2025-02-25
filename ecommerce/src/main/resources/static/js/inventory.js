function setError(id, message) {
    const errorElement = document.getElementById(id);
    if (errorElement) errorElement.innerText = message;
}

function validateInventoryForm() {
    let isValid = true;
    clearErrors();
    const groupName = document.getElementById("groupName").value;
    if (!groupName) {
        setError("groupNameError", "Group Name is required");
        isValid = false;
    }

    const fields = [
        { id: "make", message: "Make cannot be blank", minLength: 3 },
        { id: "model", message: "Model cannot be blank", minLength: 3 },
        { id: "applicableFromHsn", message: "Applicable From HSN must be in YYYYMMDD format", regex: /^\d{8}$/ },
        { id: "applicableFromGst", message: "Applicable From GST must be in YYYYMMDD format", regex: /^\d{8}$/ },
        { id: "productCode", message: "Product Code cannot be blank", minLength: 1 },
        { id: "hsnCode", message: "HSN Code must be 4 to 8 numeric digits", regex: /^[0-9]{4,8}$/ },
        { id: "itemName", message: "Item Name cannot be blank", minLength: 1 },
        { id: "openingBalance", message: "Opening Balance must be a positive number", minValue: 0 },
        { id: "openingValue", message: "Opening Value must be 0 or greater", minValue: 0 },
        { id: "igstRate", message: "IGST Rate is required", minValue: 0 }
    ];

    fields.forEach(field => {
        const value = document.getElementById(field.id)?.value.trim();
        if (
            (field.minLength && (!value || value.length < field.minLength)) ||
            (field.minValue !== undefined && (isNaN(value) || value < field.minValue)) ||
            (field.regex && !field.regex.test(value))
        ) {
            setError(`${field.id}Error`, field.message);
            isValid = false;
        }
    });

    return isValid;
}

document.addEventListener("DOMContentLoaded", () => {
    const igstRateInput = document.getElementById("igstRate");
    const cgstRateInput = document.getElementById("cgstRate");
    const sgstRateInput = document.getElementById("sgstRate");
    igstRateInput.addEventListener("input", () => {
        const igstValue = parseInt(igstRateInput.value, 10) || 0;
        const half = Math.floor(igstValue / 2);
        cgstRateInput.value = half;
        sgstRateInput.value = half;
    });
});

function setError(id, message) {
    const errorElement = document.getElementById(id);
    if (errorElement) {
        errorElement.innerText = message;
        errorElement.style.display = "block";
    }
}

function clearErrors() {
    document.querySelectorAll(".error").forEach(error => {
        error.innerText = "";
        error.style.display = "none";
    });
}

function showToastMessage(action) {
    const messages = {
        save: 'Inventory saved successfully!',
        update: 'Inventory updated successfully!',
        delete: 'Inventory deleted successfully!',
        default: 'An error occurred.',
    };

    const icon = action === 'error' ? 'error' : 'success';
    const message = messages[action] || messages.default;

    Swal.fire({
        toast: true,
        icon: icon,
        title: message,
        position: 'bottom-end',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
    });
}

document.querySelector('form').addEventListener('submit', function (event) {
    if (!validateInventoryForm()) {
        event.preventDefault();
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, '0');
    const dd = String(today.getDate()).padStart(2, '0');
    const defaultDate = `${yyyy}${mm}${dd}`;

    ['applicableFromHsn', 'applicableFromGst'].forEach(fieldId => {
        const field = document.getElementById(fieldId);
        if (field && !field.value) field.value = defaultDate;
    });
});

function softDelete(id) {
    const deleteUrl = `${urlConstant.url}/api/inventory/delete/${id}`;
    fetch(deleteUrl, { method: 'GET' })
        .then(response => {
            if (response.ok) {
                const row = document.querySelector(`tr[data-id="${id}"]`);
                if (row) {
                    row.style.display = 'none';
                }
                showToastMessage('delete');
            } else {
                response.text().then(errorMessage => {
                    console.error(`Failed to delete inventory item: ${errorMessage}`);
                    Swal.fire('Error', 'Failed to delete the inventory item.', 'error');
                });
            }
        })
        .catch(error => {
            console.error('Error while deleting:', error);
            Swal.fire('Error', 'An unexpected error occurred while deleting the item.', 'error');
        });
}

async function updateInventory(inventoryDto) {
    try {
        const dbResponse = await fetch(`${urlConstant.url}/api/inventory/update-local`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(inventoryDto),
        });

        if (dbResponse.ok) {
            Swal.fire('Updated!', 'Inventory item has been updated successfully.', 'success').then(() => {
                window.location.href = '/inventory/display?action=update&status=success';
            });
        } else {
            const errorMessage = await dbResponse.text();
            Swal.fire('Error!', `Failed to update inventory in the local database: ${errorMessage}`, 'error');
        }
    } catch (error) {
        Swal.fire('Error!', error.message, 'error');
    }
}

async function searchInventoryByMake() {
    const searchValue = document.getElementById('makeSearchInput').value.trim();
    if (searchValue.length < 2) {
        console.log('Search term too short.');
        return;
    }
    const apiUrl = `${urlConstant.url}/api/inventory/search-by-make?make=${encodeURIComponent(searchValue)}`;
    try {
        const response = await fetch(apiUrl);
        if (response.ok) {
            const inventoryData = await response.json();
            const totalItems = inventoryData.length;
            const pageSize = 25; // Items per page
            const totalPages = Math.ceil(totalItems / pageSize);
            console.log('Search Response:', inventoryData);
        } else {
            console.error('Error fetching search results:', response.status);
            Swal.fire('Error', 'Could not fetch search results.', 'error');
        }
    } catch (error) {
        console.error('Search error:', error);
        Swal.fire('Error', 'An unexpected error occurred.', 'error');
    }
}

async function showInventorySuggestions(value) {
    const suggestionsDiv = document.getElementById('inventorySuggestions');
    suggestionsDiv.innerHTML = '';
    if (value.length < 3) {
        suggestionsDiv.style.display = 'none';
        return;
    }
    try {
        const response = await fetch(`${urlConstant.url}/api/inventory/suggestions?make=${encodeURIComponent(value)}`);
        if (!response.ok) {
            console.error(`Error fetching suggestions: ${response.statusText}`);
            suggestionsDiv.style.display = 'none';
            return;
        }
        const suggestions = await response.json();
        if (suggestions.length > 0) {
            suggestionsDiv.innerHTML = suggestions.map(suggestion => `
                <div class="suggestion-item" onclick="selectInventorySuggestion('${suggestion}')">
                    ${suggestion}
                </div>
            `).join('');
            suggestionsDiv.style.display = 'block';
        } else {
            console.log('No suggestions found.');
            suggestionsDiv.style.display = 'none';
        }
    } catch (error) {
        console.error('Error while fetching suggestions:', error);
        suggestionsDiv.style.display = 'none';
    }
}

function selectInventorySuggestion(make) {
    document.getElementById('makeSearchInput').value = make;
    document.getElementById('inventorySuggestions').style.display = 'none';
}

function displayInventoryData(data) {
    const tableBody = document.querySelector('tbody');
    tableBody.innerHTML = '';
    if (data.length > 0) {
        data.forEach(item => {
            const row = `
                <tr>
                    <td>${item.groupName || 'N/A'}</td>
                    <td>${item.subGroupName || 'N/A'}</td>
                    <td>${item.make || 'N/A'}</td>
                    <td>${item.model || 'N/A'}</td>
                    <td>${item.itemName || 'N/A'}</td>
                    <td>${item.productCode || 'N/A'}</td>
                    <td>${item.hsnCode || 'N/A'}</td>
                    <td>${item.openingBalance || 'N/A'}</td>
                    <td>
                        <a href="${urlConstant.url}/inventory/edit/${item.id}" class="btn btn-warning btn-sm">Edit</a>
                        <a href="${urlConstant.url}/inventory/delete/${item.id}" class="btn btn-danger btn-sm">Delete</a>
                    </td>
                </tr>
            `;
            tableBody.innerHTML += row;
        });
    } else {
        const noDataRow = `<tr><td colspan="10" class="text-center">No Inventory Data Found</td></tr>`;
        tableBody.innerHTML = noDataRow;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const paginationButtons = document.querySelectorAll('.pagination a');
    paginationButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            event.preventDefault();
            const page = button.getAttribute('data-page');
        });
    });
});

//inventory Item name auto populate
document.addEventListener("DOMContentLoaded", () => {
    const makeInput = document.getElementById("make");
    const modelInput = document.getElementById("model");
    const productCodeInput = document.getElementById("productCode");
    const itemNameInput = document.getElementById("itemName");
    function updateItemName() {
        const make = makeInput.value.trim();
        const model = modelInput.value.trim();
        const productCode = productCodeInput.value.trim();
        const itemName = `${make}-${model}-${productCode}`.trim();
        itemNameInput.value = itemName;
    }
    makeInput.addEventListener("input", updateItemName);
    modelInput.addEventListener("input", updateItemName);
    productCodeInput.addEventListener("input", updateItemName);
});

document.addEventListener("DOMContentLoaded", () => {
    const loggedInUser = sessionStorage.getItem("loggedInUser");
    document.querySelector("input[name='createdBy']").value = loggedInUser || '';
    document.querySelector("input[name='updatedBy']").value = loggedInUser || '';
});

async function populateGroupNames() {
    const groupDropdown = document.getElementById('groupName');
    try {
        const response = await fetch(`${urlConstant.url}/api/stock-group/names`);
        if (response.ok) {
            const groupNames = await response.json();
            groupDropdown.innerHTML = '<option value="" disabled selected>Select a Group</option>';
            groupNames.forEach(group => {
                const option = document.createElement('option');
                option.value = group;
                option.textContent = group;
                groupDropdown.appendChild(option);
            });
        } else {
            console.error('Failed to fetch group names.');
        }
    } catch (error) {
        console.error('Error fetching group names:', error);
    }
}

async function submitStockGroupForm() {
    const stockGroupName = document.getElementById('stockGroupName').value.trim();
    if (!stockGroupName) {
        document.getElementById('groupNameError').style.display = 'block';
        return;
    }
    document.getElementById('groupNameError').style.display = 'none';
    try {
        const response = await fetch(`${urlConstant.url}/api/stock-group/create`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name: stockGroupName }),
        });
        if (response.ok) {
            const modal = bootstrap.Modal.getInstance(document.getElementById("createGroupModal"));
            modal.hide();
            document.getElementById("createGroupForm").reset();
            await populateGroupNames();
            Swal.fire("Success", "Stock Group created successfully!", "success").then(() => {
                window.location.reload();
            });
        } else {
            const errorMessage = await response.text();
            Swal.fire("Error", errorMessage, "error");
        }
    } catch (error) {
        console.error("Error creating stock group:", error);
        Swal.fire("Error", "An unexpected error occurred.", "error");
    }
}

function closeCreateGroupModal() {
    const modal = document.getElementById('createGroupModal');
    const bootstrapModal = bootstrap.Modal.getInstance(modal);
    bootstrapModal.hide();
    document.getElementById('createGroupForm').reset();
}

document.addEventListener('DOMContentLoaded', () => {
    populateGroupNames();
});

function validateGroupName() {
    const groupName = document.getElementById("stockGroupName").value.trim();
    if (!groupName) {
        document.getElementById("groupNameError").style.display = "block";
        return false;
    }
    document.getElementById("groupNameError").style.display = "none";
    return true;
}

document.getElementById("createGroupForm").addEventListener("submit", async function (event) {
    event.preventDefault();
    const groupNameInput = document.getElementById("stockGroupName").value.trim();
    if (!groupNameInput) {
        document.getElementById("groupNameError").style.display = "block";
        return;
    }
    document.getElementById("groupNameError").style.display = "none";
    try {
        const response = await fetch("/stock-group/create", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ stockGroupName: groupNameInput }),
        });
        if (response.ok) {
            const modal = bootstrap.Modal.getInstance(document.getElementById("createGroupModal"));
            modal.hide();
            document.getElementById("createGroupForm").reset();
            await populateGroupNames(); // Refresh the dropdown
            Swal.fire("Success", "Stock Group created successfully!", "success");
        } else {
            const errorMessage = await response.text();
            Swal.fire("Error", errorMessage, "error");
        }
    } catch (error) {
        console.error("Error creating stock group:", error);
        Swal.fire("Error", "An unexpected error occurred.", "error");
    }
});

function refreshGroupDropdown() {
    fetch("/inventory/api/groups")
        .then((response) => response.json())
        .then((groups) => {
            const dropdown = document.getElementById("groupName");
            dropdown.innerHTML = '<option value="" disabled selected>Select a Group</option>';
            groups.forEach((group) => {
                const option = document.createElement("option");
                option.value = group;
                option.textContent = group;
                dropdown.appendChild(option);
            });
        })
        .catch((error) => console.error("Error fetching group names:", error));
}

async function createStockGroup() {
    const stockGroupName = document.getElementById("stockGroupName").value;
    if (!stockGroupName) {
        alert("Stock Group Name is required!");
        return;
    }
    try {
        const response = await fetch(`${urlConstant.url}/api/stock-group/create`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ name: stockGroupName }),
        });
        if (response.ok) {
            alert("Stock Group created successfully!");
        } else {
            const errorMessage = await response.text();
            alert(`Error: ${errorMessage}`);
        }
    } catch (error) {
        console.error("Error creating stock group:", error);
    }
}

function checkGroupName(name) {
    const apiUrl = `${urlConstant.url}/api/stock-group/checkByGroupName?name=${encodeURIComponent(name)}`;
    fetch(apiUrl, {
        method: "GET",
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.text();
        })
        .then(message => {
            const resultElement = document.getElementById("resultMessage");
            if (resultElement) {
                resultElement.textContent = message;
            }
        })
}

