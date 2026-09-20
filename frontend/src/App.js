import Home from "./routes/home/home.component";
import { Routes, Route } from "react-router";
import Navigation from "./routes/navigation/navigation.component";
import Login from "./routes/login/login.component";

const Shop = () => <h2>I'm Shop page.</h2>;

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<Navigation />}>
        <Route index element={<Home />} />
        <Route path="shop" element={<Shop />} />
        <Route path="login" element={<Login />} />
      </Route>
    </Routes>
  );
};

export default App;
