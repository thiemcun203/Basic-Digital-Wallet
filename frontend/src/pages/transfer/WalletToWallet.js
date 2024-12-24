import { LoadingButton } from '@mui/lab';
import { Autocomplete, Button, Card, Grid, Stack, TextField } from '@mui/material';
import { useSnackbar } from 'notistack';
import { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet-async';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import HttpService from '../../services/HttpService';

export default function WalletToWallet() {
  const defaultValues = {
    sourceId: '',
    targetId: '',
    amount: '',
  };

  const navigate = useNavigate();
  const { enqueueSnackbar } = useSnackbar();
  const [formValues, setFormValues] = useState(defaultValues);
  const [accounts, setAccounts] = useState([]);

  // Fetch user accounts on component mount
  useEffect(() => {
    const userId = AuthService.getCurrentUser()?.customerId;
    if (userId) {
      HttpService.getWithAuth(`/account/getUserAccounts?userId=${userId}`)
        .then((response) => {
          if (response && Array.isArray(response)) {
            setAccounts(response);
          } else {
            enqueueSnackbar('No accounts found for this user.', { variant: 'warning' });
          }
        })
        .catch((error) => {
          enqueueSnackbar(error.response?.data?.message || error.message, { variant: 'error' });
        });
    }
  }, [enqueueSnackbar]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    // Convert targetId to a Long-compatible value when applicable
    setFormValues({
      ...formValues,
      [name]: name === 'targetId' ? value.replace(/\D/g, '') : value, // Strip non-numeric characters
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    // Validate fields before submission
    if (!formValues.sourceId) {
      enqueueSnackbar('Please select a source account.', { variant: 'error' });
      return;
    }
    if (!formValues.targetId) {
      enqueueSnackbar('Please enter the recipient account ID.', { variant: 'error' });
      return;
    }
    if (!formValues.amount || Number.isNaN(Number(formValues.amount)) || parseFloat(formValues.amount) <= 0) {
      enqueueSnackbar('Please enter a valid transfer amount.', { variant: 'error' });
      return;
    }
    if (parseInt(formValues.sourceId, 10) === parseInt(formValues.targetId, 10)) {
      enqueueSnackbar('Source and target accounts cannot be the same.', { variant: 'error' });
      return;
    }

    // Check if targetId exists in the database
    HttpService.getWithAuth(`/account/getAccount?accountId=${formValues.targetId}`)
      .then((response) => {
        if (!response || response.accountId !== parseInt(formValues.targetId, 10)) {
          enqueueSnackbar('The recipient account ID does not exist.', { variant: 'error' });
          return;
        }

        // Proceed with transfer
        const payload = {
          ...formValues,
          targetId: parseInt(formValues.targetId, 10), // Convert targetId to integer
        };

        HttpService.postWithAuth('/transaction/createTransaction', payload)
          .then((response) => {
            if (response.status === 'Fail') {
              enqueueSnackbar('Transaction failed: Insufficient balance.', { variant: 'error' });
            } else {
              enqueueSnackbar('Transfer completed successfully.', { variant: 'success' });
              navigate('/transactions');
            }
          })
          .catch((error) => {
            enqueueSnackbar(error.response?.data?.message || error.message, { variant: 'error' });
          });
      })
      .catch(() => {
        enqueueSnackbar('The recipient account ID does not exist.', { variant: 'error' });
      });
  };

  return (
    <>
      <Helmet>
        <title> Transfer Funds | e-Wallet </title>
      </Helmet>
      <Card>
        <Grid container direction="column" sx={{ width: 400, padding: 5 }}>
          <Stack spacing={3}>
            {/* From Account Dropdown */}
            <Autocomplete
              options={accounts}
              getOptionLabel={(account) => `Account ID: ${account.accountId} | Balance: ${account.balance}`}
              onChange={(event, newValue) =>
                setFormValues({ ...formValues, sourceId: newValue?.accountId || '' })
              }
              renderInput={(params) => <TextField {...params} label="From Account" />}
              noOptionsText="No accounts available"
            />
            {/* Recipient Account Input */}
            <TextField
              id="targetId"
              name="targetId"
              label="Recipient Account ID"
              required
              value={formValues.targetId}
              onChange={handleInputChange}
            />
            {/* Transfer Amount Input */}
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
            {/* Cancel Button */}
            <Button variant="outlined" onClick={() => navigate('/accounts')}>
              Cancel
            </Button>
            {/* Transfer Button */}
            <LoadingButton variant="contained" onClick={handleSubmit}>
              Transfer
            </LoadingButton>
          </Stack>
        </Grid>
      </Card>
    </>
  );
}
