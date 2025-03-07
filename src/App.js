import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import PaymentSuccess from "./main/resources/pages/PaymentSuccess";
import PaymentFailure from "./main/resources/pages/PaymentFailure";
import PaymentPending from "./main/resources/pages/PaymentPending";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/payment/success" element={<PaymentSuccess />} />
                <Route path="/payment/failure" element={<PaymentFailure />} />
                <Route path="/payment/pending" element={<PaymentPending />} />
            </Routes>
        </Router>
    );
}

export default App;
