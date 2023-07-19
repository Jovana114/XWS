import * as React from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import axios from "axios";
import { toast } from "react-toastify";
import Button from "@mui/material/Button";
import { useEffect, useState } from "react";

const token = localStorage.getItem("token");

export default function BasicTable() {
  const [rows, setRows] = useState<any[]>([]);
  const [search, setSearch] = useState("");
  const [EndPlace, setEndPlace] = useState("");
  const [date_and_time_taking_off, setDate_and_time_taking_off] = useState("");
  const [date_and_time_landing, setDate_and_time_landing] = useState("");
  const [number_of_tickets, setNumber_of_tickets] = useState(1);
  const [number_of_seats, setNumber_of_seats] = useState(1);

  const config = {
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  };
  const fetchData = () => {
    axios
      .get("http://localhost:8080/api/flight/all", config)
      .then(function (response) {
        if (response.data.success === false) {
          toast.error("Fetchin Failed!", {
            position: toast.POSITION.TOP_RIGHT,
          });
        } else {
          setRows(response.data);
        }
      })

      .catch(function (error) {
        console.log(error);
      });
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleDelete = async (id: any, e: any) => {
    e.preventDefault();

    const requestOptions = {
      method: "DELETE",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    };
    fetch("http://localhost:8080/api/flight/delete/" + id, requestOptions).then(
      (response) => {
        if (response.ok) {
          setRows(rows.filter((row) => row.id !== id));
          console.log("success");
        }
      }
    );
  };

  const filteredRows = rows.filter((row) => {
    return (
      row.place_taking_off.toLowerCase().includes(search.toLowerCase()) &&
      row.date_and_time_taking_off
        .toLowerCase()
        .includes(date_and_time_taking_off.toLowerCase()) &&
      row.date_and_time_landing
        .toLowerCase()
        .includes(date_and_time_landing.toLowerCase()) &&
      row.place_landing.toLowerCase().includes(EndPlace.toLowerCase()) &&
      row.number_of_tickets >= number_of_tickets &&
      row.number_of_seats >= number_of_seats
    );
  });

  return (
    <>
      <div
        style={{
          display: "inline-flex",
          alignItems: "center",
          justifyContent: "space-between",
        }}
      >
        <div style={{ padding: 20, display: "inline-block", width: "fit-content"}}>
          <div className="form-group mt-3 divSize50L">
            <label style={{ textTransform: "capitalize" }}>
              Number of tickets
            </label>
            <input
              value={number_of_tickets}
              onChange={(e) => setNumber_of_tickets(Number(e.target.value))}
              required
              type="number"
              className="form-control mt-1"
              placeholder={""}
              min={1}
            />
          </div>
          <div className="form-group mt-3 divSize50L">
            <label style={{ textTransform: "capitalize" }}>
              Number of Seats
            </label>
            <input
              value={number_of_seats}
              onChange={(e) => setNumber_of_seats(Number(e.target.value))}
              required
              type="number"
              className="form-control mt-1"
              placeholder={""}
              min={1}
            />
          </div>
        </div>
        <div style={{ padding: 20, display: "inline-block", width: "fit-content"}}>
          <div className="form-group mt-3 divSize50L">
            <label style={{ textTransform: "capitalize" }}>
              Date and time of taking off
            </label>
            <input
              value={date_and_time_taking_off}
              onChange={(e) => setDate_and_time_taking_off(e.target.value)}
              required
              type="date"
              className="form-control mt-1"
              placeholder={""}
            />
          </div>
          <div className="form-group mt-3 divSize50L">
            <label style={{ textTransform: "capitalize" }}>
              Date and time of landing
            </label>
            <input
              value={date_and_time_landing}
              onChange={(e) => setDate_and_time_landing(e.target.value)}
              required
              type="date"
              className="form-control mt-1"
              placeholder={""}
            />
          </div>
        </div>
        <div style={{ padding: 20, display: "inline-block", width: "fit-content"}}>
          <div className="form-group mt-3 divSize50L">
            <label style={{ textTransform: "capitalize" }}>Origin</label>
            <input
              type="text"
              placeholder="Start Place"
              value={search}
              className="form-control mt-1"
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>
          <div className="form-group mt-3 divSize50L">
            <label style={{ textTransform: "capitalize" }}>Destination</label>
            <input
              type="text"
              placeholder="End Place"
              value={EndPlace}
              className="form-control mt-1"
              onChange={(e) => setEndPlace(e.target.value)}
            />
          </div>
        </div>
      </div>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead style={{backgroundColor:"#ededed"}}>
            <TableRow>
              <TableCell>Date and Time of Taking Off</TableCell>
              <TableCell align="right">Origin</TableCell>
              <TableCell align="right">Date and Time of Landing</TableCell>
              <TableCell align="right">Destination</TableCell>
              <TableCell align="right">Tickets available</TableCell>
              <TableCell align="right">Number of seats</TableCell>
              <TableCell align="right">Price</TableCell>
              <TableCell align="right">Price for bought tickets</TableCell>
              <TableCell></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredRows.map((row: any) => (
              <TableRow
                key={row.id}
                sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
              >
                <TableCell component="th" scope="row">
                  {row.date_and_time_taking_off}
                </TableCell>
                <TableCell align="right">{row.place_taking_off}</TableCell>
                <TableCell align="right">{row.date_and_time_landing}</TableCell>

                <TableCell align="right">{row.place_landing}</TableCell>
                <TableCell align="right">{row.number_of_tickets}</TableCell>
                <TableCell align="right">{row.number_of_seats}</TableCell>
                <TableCell align="right">{row.price}</TableCell>
                <TableCell align="right">
                  {row.price * (row.number_of_seats - row.number_of_tickets)}
                </TableCell>
                <TableCell>
                  <Button
                    onClick={(e) => handleDelete(row.id, e)}
                    style={{ background: "#0d6efd", color: "white" }}
                  >
                    Delete
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
}