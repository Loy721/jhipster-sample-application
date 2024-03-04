import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './my-question.reducer';

export const MyQuestionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const myQuestionEntity = useAppSelector(state => state.myQuestion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="myQuestionDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.myQuestion.detail.title">MyQuestion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{myQuestionEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterSampleApplicationApp.myQuestion.name">Name</Translate>
            </span>
          </dt>
          <dd>{myQuestionEntity.name}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.myQuestion.publisher">Publisher</Translate>
          </dt>
          <dd>{myQuestionEntity.publisher ? myQuestionEntity.publisher.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/my-question" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/my-question/${myQuestionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MyQuestionDetail;
