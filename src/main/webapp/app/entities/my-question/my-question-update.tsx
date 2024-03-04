import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMyTest } from 'app/shared/model/my-test.model';
import { getEntities as getMyTests } from 'app/entities/my-test/my-test.reducer';
import { IMyQuestion } from 'app/shared/model/my-question.model';
import { getEntity, updateEntity, createEntity, reset } from './my-question.reducer';

export const MyQuestionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const myTests = useAppSelector(state => state.myTest.entities);
  const myQuestionEntity = useAppSelector(state => state.myQuestion.entity);
  const loading = useAppSelector(state => state.myQuestion.loading);
  const updating = useAppSelector(state => state.myQuestion.updating);
  const updateSuccess = useAppSelector(state => state.myQuestion.updateSuccess);

  const handleClose = () => {
    navigate('/my-question');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMyTests({}));
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
      ...myQuestionEntity,
      ...values,
      publisher: myTests.find(it => it.id.toString() === values.publisher.toString()),
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
          ...myQuestionEntity,
          publisher: myQuestionEntity?.publisher?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.myQuestion.home.createOrEditLabel" data-cy="MyQuestionCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.myQuestion.home.createOrEditLabel">Create or edit a MyQuestion</Translate>
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
                  id="my-question-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.myQuestion.name')}
                id="my-question-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                id="my-question-publisher"
                name="publisher"
                data-cy="publisher"
                label={translate('jhipsterSampleApplicationApp.myQuestion.publisher')}
                type="select"
              >
                <option value="" key="0" />
                {myTests
                  ? myTests.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/my-question" replace color="info">
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

export default MyQuestionUpdate;
