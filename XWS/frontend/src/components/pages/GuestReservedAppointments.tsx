import useHandlingReservation from "../../hooks/useHandlingReservation";
import DoubleTable from "../common/Table/DoubleTable";

const columns = [
  { key: "startDate", text: "Start Date" },
  { key: "endDate", text: "End Date" },
  { key: "numGuests", text: "Number of Guests" },
  {
    key: "id",
    text: "Cancel",
    label: "Cancel",
  },
];

const GuestReservedAppointments = () => {
  const { data, cancelReservation } = useHandlingReservation();

  const handleCancelAppointment = (e: any) => {
    cancelReservation(e);
  };

  return (
    <DoubleTable
      data={data}
      columns={columns}
      onButtonClick={(e) => handleCancelAppointment(e)}
    />
  );
};

export default GuestReservedAppointments;
