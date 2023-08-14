/* eslint-disable react-hooks/exhaustive-deps */
import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { ACCOMMODATIONS_URL } from "../constants/contsnts";

const useAccomodation = () => {
  const { setLoading } = useContext(AuthContext);
  const [data, setData] = useState([]);

  const fetchUserData = async () => {
    try {
      const response = await axiosPrivate.get(ACCOMMODATIONS_URL + "all");
      setData(response.data);
      setLoading(false);
    } catch (error) {
      toast.error("Failed to fetch accommodation data");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUserData();
  }, []);

  return data;
};
export default useAccomodation;
