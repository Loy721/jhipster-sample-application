import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tests.reducer';

export const TestsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testsEntity = useAppSelector(state => state.tests.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testsDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.tests.detail.title">Tests</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="jhipsterSampleApplicationApp.tests.id">Id</Translate>
            </span>
          </dt>
          <dd>{testsEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterSampleApplicationApp.tests.name">Name</Translate>
            </span>
          </dt>
          <dd>{testsEntity.name}</dd>
          <dt>
            <span id="questions">
              <Translate contentKey="jhipsterSampleApplicationApp.tests.questions">Questions</Translate>
            </span>
          </dt>
          <dd>{testsEntity.questions}</dd>
        </dl>
        <Button tag={Link} to="/tests" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tests/${testsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestsDetail;
