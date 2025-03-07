const mp = new MercadoPago('APP_USR-463ef703-1996-4dcf-9b9a-a2a8a23302fa'); // Replace with your Public Key

function createCheckoutButton(preferenceId) {
    const bricksBuilder = mp.bricks();

    bricksBuilder.create("wallet", "wallet_container", {
        initialization: {
            preferenceId: preferenceId,
        },
        customization: {
            texts: {
                valueProp: 'smart_option',
            },
        },
    });
}

async function fetchPreferenceId() {
    try {
        const response = await fetch('/mercadoPago/payment'); // Replace with your backend endpoint
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        createCheckoutButton(data.preferenceId);
    } catch (error) {
        console.error('Error fetching preference ID:', error);
        // Handle the error (e.g., display an error message to the user)
    }
}

fetchPreferenceId();