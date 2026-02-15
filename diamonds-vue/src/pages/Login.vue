<script setup>
import { Form, Field } from 'vee-validate';
import * as Yup from 'yup';

import { useAuthStore } from '@/stores';
import { Alert } from '@/components'

const schema = Yup.object().shape({
    username: Yup.string().required('Username is required'),
    password: Yup.string().required('Password is required')
});

async function onSubmit(values) {
    const authStore = useAuthStore();
    const { username, password } = values;
    console.log(`Calling onSubmit on form with email(${username}) and password(${password})`)
    await authStore.login(username, password);
}
</script>

<template>
  <v-layout justify="center" align-items="center">
  <v-card
    class="mx-auto my-8"
    elevation="16"
    width="30%"
    height="45%"
  >
    <v-card-item>
      <v-card-title>
        Login
      </v-card-title>

      <v-card-subtitle>
        Enter your credentials
      </v-card-subtitle>
    </v-card-item>

    <v-card-text>
      <Form class="form-container" @submit="onSubmit" :validation-schema="schema" v-slot="{ errors, isSubmitting }">
        <div class="form-group">
            <label>Email</label>
            <Field name="username" type="text" class="form-control" :class="{ 'is-invalid': errors.username }" />
            <div class="invalid-feedback">{{ errors.username }}</div>
        </div>
        <div class="form-group">
            <label>Password</label>
            <Field name="password" type="password" class="form-control" :class="{ 'is-invalid': errors.password }" />
            <div class="invalid-feedback">{{ errors.password }}</div>
        </div>
        <div class="form-group">
            <button class="btn btn-primary" :disabled="isSubmitting">
                <span v-show="isSubmitting" class="spinner-border spinner-border-sm mr-1"></span>
                Login
            </button>
        </div>
      </Form>     
      <Alert />
    </v-card-text>
  </v-card>
</v-layout>
</template>

<style scoped>
.form-container {
  display: flex;
  flex-direction: column;
}

.form-group {
  display: flex;
  flex-direction: column;

  margin: 0.5em 0;
}

.v-card {
  border: 1pt solid black;
}

</style>
