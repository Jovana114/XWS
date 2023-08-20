import { useContext } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { APPOINTMENTS_URL } from "../constants/contsnts";

enum EPrice {
  Regular,
  Holiday,
  Weekend,
  Summertime
}

enum EPricePer {
  price_per_guest,
  price_per_accommodation
}

const useAppointment = () => {
  const { setLoading } = useContext(AuthContext);

  const createAppointment = async (
    accommodationId: string,
    start: string,
    end: string,
    price_type: string,
    price_per: string,
    auto_reservation: boolean,
    price: number
  ) => {
    try {
      await axiosPrivate.post(APPOINTMENTS_URL + "create/" + accommodationId, {
        start,
        end,
        price_type,
        price_per,
        auto_reservation,
        price
      });
      toast.success("Appointment created successfully");
      setLoading(false);
    } catch (error) {
      toast.error("Failed to create appointment");
      setLoading(false);
    }
  };

  return { createAppointment };
};

export default useAppointment;
