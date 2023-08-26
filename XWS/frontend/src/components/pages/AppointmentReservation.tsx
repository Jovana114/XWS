import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  TextField,
} from "@mui/material";
import useAccomodation from "../../hooks/useAccommodation";
import DoubleTable from "../common/Table/DoubleTable";
import { useState } from "react";
import useReservation from "../../hooks/useReservation";

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
  { key: "price", text: "Price" },
  { key: "price_per", text: "Price For" },
  { key: "auto_reservation", text: "Auto reservation" },
  { key: "reserved", text: "Reserved" },
  { key: "id", text: "", label: "Reserve" },
];

const AppointmentReservation = () => {
  const { data, fetchFilteredAccommodationData } = useAccomodation(false);
  const { createReservation } = useReservation();

  const [location, setLocation] = useState("");
  const [numGuests, setNumGuests] = useState(0);
  const [start, setStart] = useState<string>(new Date().toISOString());
  const [end, setEnd] = useState<string>(new Date().toISOString());

  const [openDialog, setOpenDialog] = useState(false);

  const [selectedData, setSelectedData] = useState<any>({});

  const handleDialogSubmit = async () => {
    try {
      await createReservation(selectedData.id, {
        startDate: start,
        endDate: end,
        numGuests,
      });
      // Additional logic after successful reservation creation
      setOpenDialog(false);
    } catch (error) {
      // Handle errors if necessary
    }
  };

  const handleOpenDialog = (e: any) => {
    setOpenDialog(true);
    setSelectedData(e);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedData({});
  };

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
      <Dialog
        open={openDialog}
        onClose={handleCloseDialog}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">
          {"Confirm Reservation"}
        </DialogTitle>
        <DialogContent
          style={{
            display: "flex",
            flexDirection: "column",
            padding: "20px",
          }}
        >
          <TextField
            style={{ margin: "10px 0" }}
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
            style={{ margin: "10px 0" }}
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
          <TextField
            style={{ margin: "10px 0" }}
            type="number"
            id="numGuestsDialog"
            label="Number of Guests"
            value={numGuests}
            required
            onChange={(e) => setNumGuests(parseInt(e.target.value))}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button
            onClick={handleDialogSubmit}
            variant="contained"
            color="primary"
          >
            Confirm Reservation
          </Button>
        </DialogActions>
      </Dialog>
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
        onColumnButtonClick={(e) => handleOpenDialog(e)}
      />
    </>
  );
};

export default AppointmentReservation;
