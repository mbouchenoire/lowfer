import React, { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import {
  BrowserRouter as Router,
  Redirect,
  Route,
  Switch
} from 'react-router-dom';

import { actions } from './features/architectures/slice';
import routes, { RoutePath } from './routes';

import './App.scss';
import Header from './components/Header';

const App = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(actions.getList());
  }, [dispatch]);

  return (
    <Router>
      <>
        <div className="App">
          <header className="Header">
            <Header />
          </header>
          <div className="Content">
            <div className="Detail">
              <Switch>
                {routes.map(({ path, Component }) => (
                  <Route key={path} path={path}>
                    {Component}
                  </Route>
                ))}
                <Redirect to={RoutePath.DRAFT} />
              </Switch>
            </div>
          </div>
        </div>
      </>
    </Router>
  );
};

export default App;
