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
import { useEffect, useState } from 'react';

const token = localStorage.getItem("token");

export default function BasicTable() {
  
const [rows, setRows] = useState([])

const config = {
  headers: {
    "Content-type": "application/json",
    Authorization: `Bearer ${token}`,
  },
};
const fetchData = () => {
  axios
    .get("http://localhost:8080/api/flight/all",config)
    .then(function (response) {
      if (response.data.success === false) {
        toast.error('Fetchin Failed!', {
          position: toast.POSITION.TOP_RIGHT
      });
      } else {
          setRows(response.data)
      }
    })

    .catch(function (error) {
      console.log(error);
    });
};

useEffect(() => {
  fetchData()
})


  return rows.length > 0 ? (
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
  ):<h1>No data</h1>
}