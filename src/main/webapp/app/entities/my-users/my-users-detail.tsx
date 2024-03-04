import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './my-users.reducer';

export const MyUsersDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const myUsersEntity = useAppSelector(state => state.myUsers.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="myUsersDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.myUsers.detail.title">MyUsers</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="jhipsterSampleApplicationApp.myUsers.id">Id</Translate>
            </span>
          </dt>
          <dd>{myUsersEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterSampleApplicationApp.myUsers.name">Name</Translate>
            </span>
          </dt>
          <dd>{myUsersEntity.name}</dd>
          <dt>
            <span id="surname">
              <Translate contentKey="jhipsterSampleApplicationApp.myUsers.surname">Surname</Translate>
            </span>
          </dt>
          <dd>{myUsersEntity.surname}</dd>
        </dl>
        <Button tag={Link} to="/my-users" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/my-users/${myUsersEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MyUsersDetail;
