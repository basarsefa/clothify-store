import "./categories.style.scss";
import Categories from "./components/categories/categories.component";
import { useEffect, useState } from "react";
import api from "./axiosInstance";

const App = () => {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    api.get("/api/categories").then((response) => {
      setCategories(response.data);
    });
  });

  return <Categories categories={categories} />;
};

export default App;
