// const mp = new MercadoPago("APP_USR-463ef703-1996-4dcf-9b9a-a2a8a23302fa"), {
//     locale: 'es-AR'
// });
//
//
//     const MP = async () =>{
//         try{
//             miArticulo= {}
//             miArticulo.title = "manzana";
//             miArticulo.quantity = 1;
//             miArticulo.unit_price = 100;
//
//             const response = await fetch("api/mp",{
//                 method: 'POST',
//                 headers: {
//                     'Accept': 'Application/json',
//                     'Content-Type': 'Application/json'
//                 },
//                 body: JSON.stringify(miArticulo)
//
//             })
//
//             const  preference = await  response.text()
//             createCheckoutButton(preference)
//             console.log("preferencia: '"+preference+"'")
//         }catch (e){alert("errror: " + e)}
//
//
//         const createCheckoutButton = (preferenceId_OK)=>{
//             const bricksBuilder = mp.bricks();
//             const generateButton = async =>{
//                 if(window.checkoutButton) window.checkoutButton.unmount()
//                 bricksBuilder.create("wallet_container", {
//                     initialization: {
//                         preferenceId: preferenceId_OK,
//                     },
//                 })
//             }
//         }
//         generateButton()
//     }


const mp = new MercadoPago("APP_USR-463ef703-1996-4dcf-9b9a-a2a8a23302fa", {
    locale: 'es-AR'
});

const createCheckoutButton = (preferenceId_OK) => {
    const bricksBuilder = mp.bricks();
    const generateButton = async () => {
        try {
            if (window.checkoutButton) {
                window.checkoutButton.unmount();
            }
            window.checkoutButton = bricksBuilder.create("wallet_container", { // Assign to window for later unmounting
                initialization: {
                    preferenceId: preferenceId_OK,
                },
            });
        } catch (error) {
            console.error("Error creating checkout button:", error);
            alert("Error creating checkout button. Please try again later.");
        }
    };
    generateButton();
};

const createPreference = async (item) => {
    try {
        const response = await fetch("api/mp", {
            method: 'POST',
            headers: {
                'Accept': 'application/json', // Correct capitalization
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item)
        });

        if (!response.ok) {
            const errorData = await response.json(); // Try to parse error response
            throw new Error(`Server error: ${response.status} - ${errorData.message || response.statusText}`);
        }

        const preference = await response.text();
        console.log("Preference: '" + preference + "'");
        return preference;
    } catch (e) {
        console.error("Error creating preference:", e);
        alert("Error creating preference: " + e.message);
        return null; // or handle the error as needed
    }
}

const MP = async () => {
    const item = {
        title: "manzana",
        quantity: 1,
        unit_price: 100
    };

    const preferenceId = await createPreference(item);

    if (preferenceId) {
        createCheckoutButton(preferenceId);
    }

};

MP(); // Call the function to initiate the process
