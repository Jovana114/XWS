import { useContext } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { RESERVATION_URL } from "../constants/contsnts";

const useReservation = () => {
  const { auth, setLoading } = useContext(AuthContext);

  const createReservation = async (
    appointmentId: string,
    reservationData: {
      startDate: string;
      endDate: string;
      numGuests: number;
    }
  ) => {
    try {
      await axiosPrivate.post(
        RESERVATION_URL +`create_reservation/${appointmentId}/${auth.id}`,
        reservationData
      );
      toast.success("Reservation created successfully");
      setLoading(false);
    } catch (error) {
      toast.error("Failed to create reservation");
      setLoading(false);
    }
  };

  return { createReservation };
};

export default useReservation;
