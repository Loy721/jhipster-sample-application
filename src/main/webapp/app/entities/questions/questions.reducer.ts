import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { ASC } from 'app/shared/util/pagination.constants';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IQuestions, defaultValue } from 'app/shared/model/questions.model';

const initialState: EntityState<IQuestions> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/questions';

// Actions

export const getEntities = createAsyncThunk('questions/fetch_entity_list', async ({ sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?${sort ? `sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
  return axios.get<IQuestions[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'questions/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IQuestions>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'questions/create_entity',
  async (entity: IQuestions, thunkAPI) => {
    const result = await axios.post<IQuestions>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'questions/update_entity',
  async (entity: IQuestions, thunkAPI) => {
    const result = await axios.put<IQuestions>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'questions/partial_update_entity',
  async (entity: IQuestions, thunkAPI) => {
    const result = await axios.patch<IQuestions>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'questions/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IQuestions>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const QuestionsSlice = createEntitySlice({
  name: 'questions',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data.sort((a, b) => {
            if (!action.meta?.arg?.sort) {
              return 1;
            }
            const order = action.meta.arg.sort.split(',')[1];
            const predicate = action.meta.arg.sort.split(',')[0];
            return order === ASC ? (a[predicate] < b[predicate] ? -1 : 1) : b[predicate] < a[predicate] ? -1 : 1;
          }),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = QuestionsSlice.actions;

// Reducer
export default QuestionsSlice.reducer;
