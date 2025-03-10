import React, { useEffect, useState } from "react";
import { initMercadoPago, Wallet } from "@mercadopago/sdk-react";

const PaymentComponent = () => {
    const [preferenceId, setPreferenceId] = useState(null);

    useEffect(() => {
        initMercadoPago("APP_USR-463ef703-1996-4dcf-9b9a-a2a8a23302fa", { locale: "pt-BR" });


        fetch("http://localhost:8080/mercadoPago/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                // Passe os dados do pagamento aqui (se necessário)
            }),
        })
            .then((response) => response.text())
            .then((data) => {
                setPreferenceId(data);
            })
            .catch((error) => console.error("Erro ao obter preferenceId:", error));
    }, []);

    return (
        <div>
            {preferenceId ? (
                <Wallet initialization={{ preferenceId }} />
            ) : (
                <p>Carregando pagamento...</p>
            )}
        </div>
    );
};

export default PaymentComponent;
