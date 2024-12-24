import { LoadingButton } from '@mui/lab';
import { Autocomplete, Button, Card, Grid, Stack, TextField } from '@mui/material';
import { useSnackbar } from 'notistack';
import { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet-async';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import HttpService from '../../services/HttpService';

export default function WithdrawFunds() {
  const defaultValues = {
    sourceId: '',
    targetBankId: 'bank1', // Or any default external bank
    amount: '',
  };

  const navigate = useNavigate();
  const { enqueueSnackbar } = useSnackbar();
  const [formValues, setFormValues] = useState(defaultValues);
  const [accounts, setAccounts] = useState([]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormValues({
      ...formValues,
      [name]: value,
    });
  };

  useEffect(() => {
    const userId = AuthService.getCurrentUser()?.customerId;
    if (userId) {
      HttpService.getWithAuth(`/api/v1/account/getAllAccounts?userId=${userId}`).then((response) => {
        setAccounts(response);
      });
    }
  }, []);

  const handleSubmit = (event) => {
    event.preventDefault();
    HttpService.postWithAuth('/api/v1/transaction/createTransaction', formValues)
      .then(() => {
        enqueueSnackbar('Funds withdrawn successfully', { variant: 'success' });
        navigate('/transactions');
      })
      .catch((error) => {
        enqueueSnackbar(error.response?.data?.message || error.message, { variant: 'error' });
      });
  };

  return (
    <>
      <Helmet>
        <title> Withdraw Funds | e-Wallet </title>
      </Helmet>
      <Card>
        <Grid container direction="column" sx={{ width: 400, padding: 5 }}>
          <Stack spacing={3}>
            <Autocomplete
              options={accounts}
              getOptionLabel={(account) => `Account ${account.accountId}`}
              onChange={(event, newValue) =>
                setFormValues({ ...formValues, sourceId: newValue?.accountId || '' })
              }
              renderInput={(params) => <TextField {...params} label="From Account" />}
            />
            <TextField
              id="amount"
              name="amount"
              label="Amount"
              required
              value={formValues.amount}
              onChange={handleInputChange}
            />
          </Stack>
          <Stack spacing={2} direction="row" justifyContent="end" sx={{ mt: 4 }}>
            <Button variant="outlined" onClick={() => navigate('/accounts')}>
              Cancel
            </Button>
            <LoadingButton variant="contained" onClick={handleSubmit}>
              Withdraw
            </LoadingButton>
          </Stack>
        </Grid>
      </Card>
    </>
  );
}
