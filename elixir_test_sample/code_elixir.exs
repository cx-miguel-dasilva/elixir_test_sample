defmodule MyAppWeb.UserController do
  use MyAppWeb, :controller
  alias MyApp.Repo

  def search(conn, params) do
    username = params["username"]

    # VULNERABLE: direct string interpolation in SQL query
    query = "SELECT * FROM users WHERE username = '#{username}'"

    result = Ecto.Adapters.SQL.query!(Repo, query, [])

    json(conn, %{users: result.rows})
  end
end