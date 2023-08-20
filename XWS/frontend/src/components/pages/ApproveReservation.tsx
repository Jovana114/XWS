import { useState } from "react";
import { Button, FormControl, TextField } from "@mui/material";
import approvingReservation from "../../hooks/approvingReservation";
import DoubleTable from "../common/Table/DoubleTable";

const ApproveReservation = () => {
    
    const { data } = approvingReservation();

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
    ];

    return (<DoubleTable
        data={data}
        columns={columns}
        collapseColumn="reservations"
        collapseColumns={collapseColumns}
      />)

};

export default ApproveReservation;
