import React from "react";
import approvingReservation from "../../hooks/useApprovingReservation";
import DoubleTable from "../common/Table/DoubleTable";

const ApproveReservation = () => {
  const { data, approveReservation } = approvingReservation();

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
    {
      key: "id",
      text: "Approve",
      label: "Approve",
      render: (rowData: any) => (
        <button onClick={() => approveReservation(rowData.id)}>
          Approve
        </button>
      ),
    },
  ];

  return (
    <DoubleTable
      data={data}
      columns={columns}
      collapseColumn="reservations"
      collapseColumns={collapseColumns}
    />
  );
};

export default ApproveReservation;
