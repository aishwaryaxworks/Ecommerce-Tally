function updateTime() {
  const now = new Date();
  const time = now.toLocaleTimeString();
  const date = now.toLocaleDateString();
  document.getElementById("current-time").textContent = time;
  document.getElementById("current-date").textContent = date;
}

updateTime();
setInterval(updateTime, 1000);

async function loadCitie() {
  const selectedCountry = document.getElementById("country").value;
  const selectedState = document.getElementById("states").value;
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
