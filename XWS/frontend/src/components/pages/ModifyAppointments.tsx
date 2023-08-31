import {
  Dialog,
  DialogTitle,
  DialogActions,
  Button,
  Box,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import useAppointment from "../../hooks/useAppointment";
import DoubleTable from "../common/Table/DoubleTable";
import { useState } from "react";

const columns = [
  { key: "start", text: "Start Date" },
  { key: "end", text: "End Date" },
  { key: "reserved", text: "Reserved" },
  { key: "price_type", text: "Price Type" },
  { key: "price_per", text: "Price Per" },
  { key: "auto_reservation", text: "Auto Reservation" },
  { key: "price", text: "Price" },
  {
    key: "id",
    text: "Modify",
    label: "Modify",
  },
];

const ModifyAppointments = () => {
  const { data, getAppointmentById, modifyAppointment } = useAppointment();

  const [appointmentId, setAppointmentId] = useState("");
  const [start, setStart] = useState<string>(new Date().toISOString());
  const [end, setEnd] = useState<string>(new Date().toISOString());
  const [priceType, setPriceType] = useState<any>("");
  const [pricePer, setPricePer] = useState<any>("");
  const [autoReservation, setAutoReservation] = useState(false);
  const [price, setPrice] = useState(0);

  const handleSubmit = () => {
    modifyAppointment(
      appointmentId,
      start,
      end,
      priceType,
      pricePer,
      autoReservation,
      price
    );
    handleClose();
  };

  const [open, setOpen] = useState(false);
  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };
  const handleModifyAppointment = async (e: any) => {
    if (e !== null) {
      setAppointmentId(e);
      const appointment: any = await getAppointmentById(e);
      if (appointment !== null) {
        setStart(appointment.start);
        setEnd(appointment.end);
        setPriceType(appointment.price_type); // Removed JSON.stringify
        setPricePer(appointment.price_per); // Removed JSON.stringify
        setAutoReservation(appointment.auto_reservation);
        setPrice(appointment.price);
        // setReserved(appointment.reserved);
      }
    }
    handleOpen();
  };

  return (
    <>
      <DoubleTable
        data={data}
        columns={columns}
        onButtonClick={(e) => handleModifyAppointment(e)}
      />
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">
          {"Confirm Appointment Modification"}
        </DialogTitle>

        <Box
          display="flex"
          flexDirection="column"
          sx={{ alignItems: "center" }}
        >
          <FormControl
            fullWidth
            required
            sx={{ width: "200px", margin: "10px" }}
          >
            <InputLabel id="PriceType-label">Price Type</InputLabel>
            <Select
              labelId="PriceType-label"
              id="PriceType"
              value={priceType}
              label="Price Type"
              onChange={(e) => setPriceType(e.target.value)}
            >
              <MenuItem value="Regular">Regular</MenuItem>
              <MenuItem value="Holiday">Holiday</MenuItem>
              <MenuItem value="Weekend">Weekend</MenuItem>
              <MenuItem value="Summertime">Summertime</MenuItem>
            </Select>
          </FormControl>
          <FormControl
            fullWidth
            required
            sx={{ width: "200px", margin: "10px 0" }}
          >
            <InputLabel id="PricePer-label">Price For</InputLabel>
            <Select
              labelId="PricePer-label"
              id="PricePer"
              value={pricePer}
              label="Price Type"
              onChange={(e) => setPricePer(e.target.value)}
            >
              <MenuItem value="price_per_guest">Guest</MenuItem>
              <MenuItem value="price_per_accommodation">Accommodation</MenuItem>
            </Select>
          </FormControl>
          <FormControl
            fullWidth
            required
            sx={{ width: "200px", margin: "10px 0" }}
          >
            <InputLabel id="autoReservation-label">Auto Reservation</InputLabel>
            <Select
              labelId="autoReservation-label"
              id="autoReservation"
              value={autoReservation}
              label="Auto Reservation"
              onChange={(e) =>
                setAutoReservation(e.target.value === "true" ? true : false)
              }
            >
              <MenuItem value="true">True</MenuItem>
              <MenuItem value="false">False</MenuItem>
            </Select>
          </FormControl>
          <TextField
            label="Start"
            type="datetime-local"
            value={start.slice(0, 16)} // Truncate seconds and milliseconds
            onChange={(e) => setStart(e.target.value)}
            fullWidth
            required
            sx={{ width: "200px", margin: "10px 0" }}
          />

          <TextField
            label="End"
            type="datetime-local"
            value={end.slice(0, 16)} // Truncate seconds and milliseconds
            onChange={(e) => setEnd(e.target.value)}
            fullWidth
            required
            sx={{ width: "200px", margin: "10px 0" }}
          />
          <TextField
            label="Price"
            type="number"
            value={price}
            onChange={(e) => setPrice(parseInt(e.target.value))}
            fullWidth
            required
            inputProps={{
              min: 0,
            }}
            sx={{ width: "200px", margin: "10px 0" }}
          />
        </Box>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained" color="primary">
            Confirm Modification
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default ModifyAppointments;
