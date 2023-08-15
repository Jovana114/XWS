/* eslint-disable react-hooks/exhaustive-deps */
import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { ACCOMMODATIONS_URL } from "../constants/contsnts";

const useAccomodation = () => {
  const { setLoading } = useContext(AuthContext);
  const [data, setData] = useState([]);

  const fetchAccommodationData = async () => {
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

  const fetchFilteredAccommodationData = async (
    location: string,
    numGuests: number,
    start: string,
    end: string
  ) => {
    try {
      const response = await axiosPrivate.get(
        ACCOMMODATIONS_URL + "search/accommodations",
        {
          params: {
            location,
            numGuests,
            start, // Convert to 'yyyy-MM-dd'T'HH:mm:ss' format
            end, // Convert to 'yyyy-MM-dd'T'HH:mm:ss' format
          },
        }
      );
      setData(response.data);
      setLoading(false);
    } catch (error) {
      toast.error("Failed to fetch accommodation data");
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    fetchAccommodationData();
  }, []);

  return { data, fetchFilteredAccommodationData };
};
export default useAccomodation;
