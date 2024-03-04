import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './questions.reducer';

export const QuestionsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const questionsEntity = useAppSelector(state => state.questions.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionsDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.questions.detail.title">Questions</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="jhipsterSampleApplicationApp.questions.id">Id</Translate>
            </span>
          </dt>
          <dd>{questionsEntity.id}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="jhipsterSampleApplicationApp.questions.content">Content</Translate>
            </span>
          </dt>
          <dd>{questionsEntity.content}</dd>
        </dl>
        <Button tag={Link} to="/questions" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/questions/${questionsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionsDetail;
