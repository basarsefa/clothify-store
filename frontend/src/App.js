import Home from "./routes/home/home.component";
import { Routes, Route } from "react-router";
import Navigation from "./routes/navigation/navigation.component";
import Login from "./routes/login/login.component";
import VerifyEmail from "./routes/verify-email/verify-email.component";
import ResetPassword from "./routes/reset-password/reset-password.component";

const Shop = () => <h2>I'm Shop page.</h2>;

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<Navigation />}>
        <Route index element={<Home />} />
        <Route path="shop" element={<Shop />} />
        <Route path="login" element={<Login />} />
        <Route path="verify-email" element={<VerifyEmail />} />
        <Route path="reset-password" element={<ResetPassword />} />
      </Route>
    </Routes>
  );
};

export default App;
