/* eslint-disable react-hooks/exhaustive-deps */
import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { ACCOMMODATIONS_URL } from "../constants/contsnts";

const useAccomodation = () => {
  const { auth, setLoading } = useContext(AuthContext);
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

  const createAccomodation = async (
    name: string,
    location: string,
    benefits: string,
    min_guests: number,
    max_guests: number
  ) => {
    try {
      const response = await axiosPrivate.post(
        ACCOMMODATIONS_URL + "create/" + auth.id,
        {
          name,
          location,
          benefits,
          min_guests,
          max_guests,
        }
      );
      toast.success("Successfully created accommodation");
      setData(response.data);
      setLoading(false);
    } catch (error) {
      toast.error("Failed to create accommodation");
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    fetchAccommodationData();
  }, []);

  return { data, fetchFilteredAccommodationData, createAccomodation };
};
export default useAccomodation;
