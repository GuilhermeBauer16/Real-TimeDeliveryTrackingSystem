import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import PaymentSuccess from "./pages/PaymentSuccess";
import PaymentFailure from "./pages/PaymentFailure";
import PaymentPending from "./pages/PaymentPending";

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
