import Categories from "../../components/categories/categories.component";
import { useEffect, useState } from "react";
import api from "../../axiosInstance";
import { Outlet } from "react-router";

const Home = () => {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    api.get("/api/categories").then((response) => {
      setCategories(response.data);
    });
  }, []);

  return (
    <div>
      <Categories categories={categories} />
      <Outlet></Outlet>
    </div>
  );
};

export default Home;
