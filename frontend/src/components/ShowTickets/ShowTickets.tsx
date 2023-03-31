import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import axios from 'axios';
import { toast } from 'react-toastify';
import Button from "@mui/material/Button";
import { useEffect, useState } from 'react';
import Dialog from "@mui/material/Dialog";

interface ShowTicketsProps {
  open: boolean;
  onClose: () => void;
}

export default function ShowTickets({ open, onClose }: ShowTicketsProps) {
  
const token = localStorage.getItem("token");

const [rows, setRows] = useState([])

const config = {
  headers: {
    "Content-type": "application/json",
    Authorization: `Bearer ${token}`,
  },
};

const user_id = JSON.parse(localStorage.getItem("id")!);

useEffect(() => {
  (async () => {
    axios
      .get("http://localhost:8080/api/flight/allTicketsPerUser/" + user_id, config)
      .then((res) => setRows(res.data));
  })(); 
}, [setRows]);

const handleClose = () => {
  onClose();
};

  return (
    <>
    <Dialog onClose={handleClose} open={open}>
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>Date and Time of Taking Off</TableCell>
            <TableCell align="right">Place of taking off</TableCell>
            <TableCell align="right">Date and Time of Landing</TableCell>
            <TableCell align="right">Place of landing</TableCell>
            <TableCell align="right">Number of tickets</TableCell>
            <TableCell align="right">Price</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row:any) => (
            <TableRow
              key={row.id}
              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                {row.date_and_time_taking_off}
              </TableCell>
              <TableCell align="right">{row.place_taking_off}</TableCell>
              <TableCell align="right">{row.date_and_time_landing}</TableCell>
              <TableCell align="right">{row.place_landing}</TableCell>
              <TableCell align="right">{row.number_of_tickets}</TableCell>
              <TableCell align="right">{row.price}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
    </Dialog>
    </>
  );
}