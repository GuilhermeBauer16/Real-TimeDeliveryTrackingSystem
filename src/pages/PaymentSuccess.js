import { useEffect } from "react";
import { useLocation } from "react-router-dom";

function PaymentSuccess() {
    const location = useLocation();

    useEffect(() => {
        console.log("Payment Success:", location.search); // Log payment details
    }, [location]);

    return <h1>✅ Payment Successful! Thank you for your purchase.</h1>;
}

export default PaymentSuccess;

