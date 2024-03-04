import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFiles } from 'app/shared/model/files.model';
import { getEntities as getFiles } from 'app/entities/files/files.reducer';
import { ITests } from 'app/shared/model/tests.model';
import { getEntity, updateEntity, createEntity, reset } from './tests.reducer';

export const TestsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const files = useAppSelector(state => state.files.entities);
  const testsEntity = useAppSelector(state => state.tests.entity);
  const loading = useAppSelector(state => state.tests.loading);
  const updating = useAppSelector(state => state.tests.updating);
  const updateSuccess = useAppSelector(state => state.tests.updateSuccess);

  const handleClose = () => {
    navigate('/tests');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...testsEntity,
      ...values,
      files: files.find(it => it.id.toString() === values.files.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...testsEntity,
          files: testsEntity?.files?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.tests.home.createOrEditLabel" data-cy="TestsCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.tests.home.createOrEditLabel">Create or edit a Tests</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="tests-id"
                  label={translate('jhipsterSampleApplicationApp.tests.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.tests.name')}
                id="tests-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.tests.questions')}
                id="tests-questions"
                name="questions"
                data-cy="questions"
                type="text"
              />
              <ValidatedField
                id="tests-files"
                name="files"
                data-cy="files"
                label={translate('jhipsterSampleApplicationApp.tests.files')}
                type="select"
              >
                <option value="" key="0" />
                {files
                  ? files.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tests" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TestsUpdate;
