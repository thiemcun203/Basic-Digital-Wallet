import {
  Card,
  Container,
  IconButton,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TablePagination,
  TableRow,
  Typography,
} from '@mui/material';
import { sentenceCase } from 'change-case';
import { enqueueSnackbar } from 'notistack';
import { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet-async';
import { useNavigate } from 'react-router-dom';
import Iconify from '../../components/iconify';
import Label from '../../components/label';
import Scrollbar from '../../components/scrollbar';
import AuthService from '../../services/AuthService';
import HttpService from '../../services/HttpService';
import TransactionListHead from './TransactionListHead';

const TABLE_HEAD = [
  { id: 'transactionId', label: 'Transaction ID', alignRight: false, firstColumn: true },
  { id: 'sender', label: 'Sender', alignRight: false },
  { id: 'receiver', label: 'Receiver', alignRight: false },
  { id: 'amount', label: 'Amount', alignRight: true },
  { id: 'description', label: 'Description', alignRight: false },
  { id: 'status', label: 'Status', alignRight: false },
  { id: '' },
];

export default function Transaction() {
  const [open, setOpen] = useState(null);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [data, setData] = useState([]);
  const navigate = useNavigate();

  const handleOpenMenu = (event) => {
    setOpen(event.currentTarget);
  };

  const handleCloseMenu = () => {
    setOpen(null);
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setPage(0);
    setRowsPerPage(parseInt(event.target.value, 10));
  };

  const emptyRows = page > 0 ? Math.max(0, (1 + page) * rowsPerPage - data.length) : 0;

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = () => {
    const currentUser = AuthService.getCurrentUser();
    const customerId = currentUser?.customerId;

    if (!customerId) {
      enqueueSnackbar('User not logged in.', { variant: 'error' });
      navigate('/login');
      return;
    }

    HttpService.getWithAuth(`/transaction/getUserTransactions?userId=${customerId}`)
      .then((response) => {
        const formattedData = response.map((item) => ({
          transactionId: item.transactionId,
          sender: item.sourceId || 'N/A',
          receiver: item.targetId || 'N/A',
          amount: item.amount,
          description: item.description || 'N/A',
          status: item.status,
        }));
        setData(formattedData);
      })
      .catch((error) => {
        if (error?.response?.status === 401) {
          enqueueSnackbar('Session expired. Please log in again.', { variant: 'error' });
          navigate('/login');
        } else {
          enqueueSnackbar(error.message, { variant: 'error' });
        }
      });
  };

  return (
    <>
      <Helmet>
        <title> Transactions | e-Transaction </title>
      </Helmet>
      <Container sx={{ minWidth: '100%' }}>
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={1}>
          <Typography variant="h4" gutterBottom>
            My Transactions
          </Typography>
        </Stack>
        <Card>
          <Scrollbar>
            <TableContainer sx={{ minWidth: 800 }}>
              <Table>
                <TransactionListHead headLabel={TABLE_HEAD} />
                <TableBody>
                  {data.length > 0 ? (
                    data.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row) => {
                      const {
                        transactionId,
                        sender,
                        receiver,
                        amount,
                        description,
                        status,
                      } = row;
                      return (
                        <TableRow hover key={transactionId} tabIndex={-1} role="checkbox">
                          <TableCell align="left" sx={{ paddingLeft: 5 }}>
                            {transactionId}
                          </TableCell>
                          <TableCell align="left">{sender}</TableCell>
                          <TableCell align="left">{receiver}</TableCell>
                          <TableCell align="right">{amount}</TableCell>
                          <TableCell align="left">{description}</TableCell>
                          <TableCell align="left">
                            <Label
                              color={status.toLowerCase() === 'success' ? 'success' : 'error'}
                            >
                              {sentenceCase(status)}
                            </Label>
                          </TableCell>
                          <TableCell align="right" width="20">
                            <IconButton size="large" color="inherit" onClick={handleOpenMenu}>
                              <Iconify icon={'eva:more-vertical-fill'} />
                            </IconButton>
                          </TableCell>
                        </TableRow>
                      );
                    })
                  ) : (
                    <TableRow>
                      <TableCell align="center" colSpan={8}>
                        No transactions found.
                      </TableCell>
                    </TableRow>
                  )}
                  {emptyRows > 0 && (
                    <TableRow style={{ height: 53 * emptyRows }}>
                      <TableCell colSpan={8} />
                    </TableRow>
                  )}
                </TableBody>
              </Table>
            </TableContainer>
          </Scrollbar>
          <TablePagination
            rowsPerPageOptions={[5, 10, 25]}
            component="div"
            count={data.length}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </Card>
      </Container>
    </>
  );
}
