import useHandlingReservation from "../../hooks/useHandlingReservation";
import DoubleTable from "../common/Table/DoubleTable";

const columns = [
  { key: "start", text: "Start Date" },
  { key: "end", text: "End Date" },
  { key: "price", text: "Price" },
  { key: "price_per", text: "Price For" },
];

const collapseColumns = [
  { key: "startDate", text: "Start Date" },
  { key: "endDate", text: "End Date" },
  { key: "numGuests", text: "Number of guests" },
  { key: "users_cancellation_number", text: "User's cancellation number" },
  {
    key: "id",
    text: "Approve",
    label: "Approve",
  },
];

const ApproveReservation = () => {
  const { data, approveReservation } = useHandlingReservation();

  const handleApproveAppointment = (e: any) => {
    approveReservation(e.id);
  };

  return (
    <DoubleTable
      data={data}
      columns={columns}
      collapseColumn="reservations"
      collapseColumns={collapseColumns}
      onColumnButtonClick={(e) => handleApproveAppointment(e)}
    />
  );
};

export default ApproveReservation;
