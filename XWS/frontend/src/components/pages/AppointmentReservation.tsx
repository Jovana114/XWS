import { Button, FormControl, TextField } from "@mui/material";
import useAccomodation from "../../hooks/useAccommodation";
import DoubleTable from "../common/Table/DoubleTable";
import { useState } from "react";

const AppointmentReservation = () => {
  const { data, fetchFilteredAccommodationData } = useAccomodation(false);
  const [location, setLocation] = useState("");
  const [numGuests, setNumGuests] = useState(0);
  const [start, setStart] = useState<string>(new Date().toISOString());
  const [end, setEnd] = useState<string>(new Date().toISOString());

  const columns = [
    { key: "benefits", text: "Benefits" },
    { key: "location", text: "Location" },
    { key: "max_guests", text: "Max number of guests" },
    { key: "min_guests", text: "Min number of guests" },
    { key: "name", text: "Name" },
  ];
  const collapseColumns = [
    { key: "start", text: "Start Date" },
    { key: "end", text: "End Date" },
    { key: "id", text: "", label: "Reserve"}
  ];

  const handleFilters = async () => {
    fetchFilteredAccommodationData(
      location,
      numGuests,
      formatDateTime(start),
      formatDateTime(end)
    );
  };
  const disableForm = location !== "" && numGuests > 0 && start && end;

  const formatDateTime = (dateTimeString: string): string => {
    const date = new Date(dateTimeString);
    return date.toISOString().slice(0, 19);
  };

  return (
    <>
      <div>
        <FormControl
          variant="outlined"
          style={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "center",
          }}
        >
          <TextField
            style={{ margin: "0 10px" }}
            type="text"
            id="location"
            label="Location"
            value={location}
            required
            onChange={(e) => setLocation(e.target.value)}
          />
          <TextField
            style={{ margin: "0 10px", width: "150px" }}
            type="number"
            id="numGuests"
            label="Number of Guests"
            value={numGuests}
            required
            onChange={(e) => setNumGuests(parseInt(e.target.value))}
          />
          <TextField
            style={{ margin: "0 10px" }}
            type="datetime-local"
            id="start"
            label="Arriving Date"
            value={start}
            required
            onChange={(e) => setStart(e.target.value)}
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            style={{ margin: "0 10px" }}
            type="datetime-local"
            id="end"
            label="Departure Date"
            value={end}
            required
            onChange={(e) => setEnd(e.target.value)}
            InputLabelProps={{
              shrink: true,
            }}
          />

          <Button disabled={!disableForm} onClick={handleFilters}>
            Filter Search
          </Button>
        </FormControl>
      </div>
      <DoubleTable
        data={data}
        columns={columns}
        collapseColumn="appointments"
        collapseColumns={collapseColumns}
      />
    </>
  );
};

export default AppointmentReservation;
