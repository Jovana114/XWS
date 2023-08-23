import React, { useState } from "react";
import Box from "@mui/material/Box";
import Collapse from "@mui/material/Collapse";
import IconButton from "@mui/material/IconButton";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import KeyboardArrowUpIcon from "@mui/icons-material/KeyboardArrowUp";
import { Button } from "@mui/material";
import Skeleton from "@mui/material/Skeleton";

interface CollapsibleTableProps {
  data: Array<{ [key: string]: any }> | null | undefined;
  columns: Array<{ key: string; text: string }>;
  collapseColumn?: string;
  collapseColumns?: Array<{ key: string; text: string; label?: string }>;
  onButtonClick?: (rowData: any) => void;
  onColumnButtonClick?: (rowData: any) => void;
}

function DoubleTable({
  data,
  columns,
  collapseColumn,
  collapseColumns,
  onButtonClick,
  onColumnButtonClick,
}: CollapsibleTableProps) {
  const [openRows, setOpenRows] = useState<string[]>([]);

  const handleRowClick = (rowKey: string) => {
    if (collapseColumn) {
      setOpenRows((prevOpenRows) =>
        prevOpenRows.includes(rowKey)
          ? prevOpenRows.filter((key) => key !== rowKey)
          : [...prevOpenRows, rowKey]
      );
    }
  };

  return (
    <TableContainer
      component={Paper}
      style={{ boxShadow: "none" }}
      className="tableRow"
    >
      <Table aria-label="collapsible table">
        <TableHead style={{ borderBottom: "1px solid rgba(0,0,0,0.1)" }}>
          <TableRow>
            {collapseColumn && <TableCell />}
            {columns.map((column) => (
              <TableCell key={column.key}>{column.text}</TableCell>
            ))}
          </TableRow>
        </TableHead>
        <TableBody>
          {data === null ||
          data === undefined ||
          data.length === 0 ||
          data[0] === null ? (
            <TableRow>
              <TableCell sx={{ padding: "0" }} colSpan={columns.length + 1}>
                <Skeleton
                  variant="text"
                  animation="wave"
                  width="100%"
                  height="80px"
                />
              </TableCell>
            </TableRow>
          ) : (
            data.map((row, index) => (
              <React.Fragment key={index}>
                <TableRow
                  sx={{ "& > *": { borderBottom: "unset" } }}
                  onClick={() => handleRowClick(row.key || index.toString())}
                >
                  {collapseColumn && (
                    <TableCell>
                      <IconButton aria-label="expand row" size="small">
                        {openRows.includes(row.key || index.toString()) ? (
                          <KeyboardArrowUpIcon />
                        ) : (
                          <KeyboardArrowDownIcon />
                        )}
                      </IconButton>
                    </TableCell>
                  )}
                  {columns.map((column: any) => (
                    <TableCell key={column.key}>
                      {column.key === "id" ? (
                        <Button
                          onClick={() =>
                            onButtonClick && onButtonClick(row[column.key])
                          }
                        >
                          {column.label}
                        </Button>
                      ) : column.key === "image" ? ( // Check for the last column with key "image"
                        <img
                          src={row[column.key]} // Assuming the URL of the image is stored in the data
                          alt="Item Image"
                          width={50} // Set your desired width
                          height={50} // Set your desired height
                        />
                      ) : (
                        row[column.key]
                      )}
                    </TableCell>
                  ))}
                </TableRow>
                {collapseColumn &&
                  (row[collapseColumn]?.length ?? 0) > 0 &&
                  openRows.includes(row.key || index.toString()) && (
                    <TableRow>
                      <TableCell
                        style={{ paddingBottom: 0, paddingTop: 0 }}
                        colSpan={columns.length + 1}
                      >
                        <Collapse in={true} timeout="auto" unmountOnExit>
                          <Box
                            sx={{
                              width: " 100%",
                              borderLeft: "1px solid rgba(0,0,0,0.1)",
                              borderRight: "1px solid rgba(0,0,0,0.1)",
                            }}
                          >
                            <Table
                              aria-label="purchases"
                              className="custom-table"
                            >
                              <TableHead
                                style={{
                                  borderBottom: "1px solid rgba(0,0,0,0.1)",
                                }}
                              >
                                {/* ... (other table head structure) */}
                              </TableHead>
                              <TableBody>
                                {row[collapseColumn]?.map(
                                  (item: any, itemIndex: number) => (
                                    <TableRow
                                      key={itemIndex}
                                      style={{
                                        borderTop: "1px solid rgba(0,0,0,0.1)",
                                      }}
                                    >
                                      {collapseColumns &&
                                        collapseColumns.map((column: any) => (
                                          <TableCell
                                            key={column.key}
                                            style={{
                                              border: "none",
                                            }}
                                          >
                                            {column.key === "id" ? (
                                              <Button
                                                onClick={() =>
                                                  onColumnButtonClick &&
                                                  onColumnButtonClick(item)
                                                }
                                              >
                                                {column.label}
                                              </Button>
                                            ) : (
                                              item[column.key]
                                            )}
                                          </TableCell>
                                        ))}
                                    </TableRow>
                                  )
                                )}
                              </TableBody>
                            </Table>
                          </Box>
                        </Collapse>
                      </TableCell>
                    </TableRow>
                  )}
              </React.Fragment>
            ))
          )}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

export default DoubleTable;