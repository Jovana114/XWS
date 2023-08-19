/* eslint-disable react-hooks/exhaustive-deps */
import { useContext } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { APPOINTMENTS_URL } from "../constants/contsnts";

const useAppointment = () => {
  const { setLoading } = useContext(AuthContext);

  const createAppointment = async (
    accommodationId: string,
    start: string,
    end: string,
    price_per_guest: number,
    price_per_accommodation: number
  ) => {
    try {
      await axiosPrivate.post(APPOINTMENTS_URL + "create/" + accommodationId, {
        start,
        end,
        price_per_guest,
        price_per_accommodation,
      });
      toast.success("Appointment created successfully");
      // Handle the response as needed
      setLoading(false);
    } catch (error) {
      toast.error("Failed to create appointment");
      setLoading(false);
    }
  };

  return { createAppointment };
};

export default useAppointment;
